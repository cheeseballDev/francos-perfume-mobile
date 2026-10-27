package com.example.francosperfumemobile.activities;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.branchdto.BranchDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.CreateRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.CreateRequestItemDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestProductFilterDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.CreateRequestResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestFiltersResponse;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.DialogHelper;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.example.francosperfumemobile.helpers.StatusStyle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * New transfer request, same rules as the website's CreateTransferRequestPage:
 *  - pick a source and a destination branch (destination can't be the source or the warehouse)
 *  - add products from the source branch; you can't ask for more than it has, except from the warehouse
 *  - optional message (max 255), then confirm and send
 */
public class CreateRequestActivity extends BaseActivity {

    private static final int WAREHOUSE_MAX = 99; // website: ProductSelector maxQty for the warehouse

    private final List<BranchDTO> branches = new ArrayList<>();
    private final List<RequestProductFilterDTO> products = new ArrayList<>();
    /** productId → line (keeps the order products were added) */
    private final Map<Integer, Line> lines = new LinkedHashMap<>();

    private BranchDTO fromBranch, toBranch;
    private RequestProductFilterDTO selectedProduct;

    private MaterialAutoCompleteTextView dropdownFrom, dropdownTo, dropdownProduct;
    private TextInputLayout layoutProduct, layoutQuantity;
    private TextInputEditText editTextQuantity, editTextMessage;
    private TextView textViewAvailable, textViewProductsHint, textViewNoLines, textViewSummary, textViewRoute;
    private LinearLayout layoutLines;
    private MaterialButton buttonAdd, buttonSubmit;

    private static final class Line {
        final int productId;
        final String name;
        int quantity;

        Line(int productId, String name, int quantity) {
            this.productId = productId;
            this.name = name;
            this.quantity = quantity;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) return;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_request);

        View root = findViewById(R.id.create_request_root);
        View topBar = findViewById(R.id.top_bar_create_request);
        InsetsHelper.applyEdgeToEdge(root, topBar, null, true);
        setupDetailTopBar(topBar, getString(R.string.create_request_title), null);

        dropdownFrom = findViewById(R.id.dropdown_create_from_branch);
        dropdownTo = findViewById(R.id.dropdown_create_to_branch);
        dropdownProduct = findViewById(R.id.dropdown_create_product);
        layoutProduct = findViewById(R.id.text_input_layout_product);
        layoutQuantity = findViewById(R.id.text_input_layout_create_quantity);
        editTextQuantity = findViewById(R.id.edit_text_create_quantity);
        editTextMessage = findViewById(R.id.edit_text_create_message);
        textViewAvailable = findViewById(R.id.text_view_create_available);
        textViewProductsHint = findViewById(R.id.text_view_create_products_hint);
        textViewNoLines = findViewById(R.id.text_view_create_no_lines);
        textViewSummary = findViewById(R.id.text_view_create_summary);
        textViewRoute = findViewById(R.id.text_view_create_route);
        layoutLines = findViewById(R.id.layout_create_lines);
        buttonAdd = findViewById(R.id.button_create_add_product);
        buttonSubmit = findViewById(R.id.button_create_submit);

        findViewById(R.id.button_create_minus).setOnClickListener(v -> setQuantity(quantity() - 1));
        findViewById(R.id.button_create_plus).setOnClickListener(v -> setQuantity(quantity() + 1));
        editTextQuantity.addTextChangedListener(new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                layoutQuantity.setError(null);
            }
        });
        buttonAdd.setOnClickListener(v -> addProduct());
        buttonSubmit.setOnClickListener(v -> confirmSubmit());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (lines.isEmpty() && fromBranch == null) {
                    finish();
                    return;
                }
                DialogHelper.confirm(CreateRequestActivity.this, getString(R.string.discard_title),
                        getString(R.string.create_discard_message), getString(R.string.discard), true,
                        CreateRequestActivity.this::finish);
            }
        });

        refreshSummary();
        loadBranches();
    }

    // ---------------------------------------------------------------- branches

    private void loadBranches() {
        new RequestRepository(this).getRequestFilters().enqueue(new SafeCallback<RequestFiltersResponse>(this) {
            @Override
            public void onSuccess(@Nullable RequestFiltersResponse response) {
                branches.clear();
                if (response != null && response.getBranches() != null) branches.addAll(response.getBranches());
                List<String> names = new ArrayList<>();
                for (BranchDTO b : branches) names.add(b.getBranchLocation());
                dropdownFrom.setAdapter(new FilterManager.NoFilterAdapter(CreateRequestActivity.this, names));
                dropdownFrom.setOnItemClickListener((parent, view, position, id) -> setFromBranch(branches.get(position)));
                updateToBranchOptions();
            }
        });
    }

    private void setFromBranch(BranchDTO branch) {
        if (fromBranch != null && fromBranch.getBranchId() == branch.getBranchId()) return;
        if (!lines.isEmpty()) {
            // Products depend on the source branch; start the list again
            lines.clear();
            renderLines();
            Toast.makeText(this, R.string.create_lines_cleared, Toast.LENGTH_SHORT).show();
        }
        fromBranch = branch;
        if (toBranch != null && toBranch.getBranchId() == branch.getBranchId()) {
            toBranch = null;
            dropdownTo.setText("", false);
        }
        updateToBranchOptions();
        loadProducts(branch.getBranchId());
        refreshSummary();
    }

    /** Destination: any branch except the source and the warehouse (website: BranchSelector). */
    private void updateToBranchOptions() {
        List<BranchDTO> options = new ArrayList<>();
        for (BranchDTO b : branches) {
            boolean isSource = fromBranch != null && b.getBranchId() == fromBranch.getBranchId();
            boolean isWarehouse = b.getBranchLocation() != null && b.getBranchLocation().toUpperCase(Locale.ROOT).contains("WAREHOUSE");
            if (!isSource && !isWarehouse) options.add(b);
        }
        List<String> names = new ArrayList<>();
        for (BranchDTO b : options) names.add(b.getBranchLocation());
        dropdownTo.setAdapter(new FilterManager.NoFilterAdapter(this, names));
        dropdownTo.setOnItemClickListener((parent, view, position, id) -> {
            toBranch = options.get(position);
            refreshSummary();
        });
    }

    private boolean isFromWarehouse() {
        return fromBranch != null && fromBranch.getBranchLocation() != null
                && fromBranch.getBranchLocation().toUpperCase(Locale.ROOT).equals("WAREHOUSE");
    }

    // ---------------------------------------------------------------- products

    private void loadProducts(int branchId) {
        products.clear();
        selectedProduct = null;
        dropdownProduct.setText("", false);
        layoutProduct.setEnabled(false);
        buttonAdd.setEnabled(false);
        textViewAvailable.setVisibility(View.GONE);
        textViewProductsHint.setText(R.string.create_loading_products);

        new RequestRepository(this).getProductsForBranch(branchId).enqueue(new SafeCallback<RequestFiltersResponse>(this) {
            @Override
            public void onSuccess(@Nullable RequestFiltersResponse response) {
                if (fromBranch == null || fromBranch.getBranchId() != branchId) return; // user changed branch meanwhile
                if (response != null && response.getProducts() != null) products.addAll(response.getProducts());
                List<String> labels = new ArrayList<>();
                for (RequestProductFilterDTO p : products) {
                    labels.add(isFromWarehouse() ? p.getProductName()
                            : getString(R.string.create_product_option, p.getProductName(), (int) p.getProductQty()));
                }
                dropdownProduct.setAdapter(new FilterManager.NoFilterAdapter(CreateRequestActivity.this, labels));
                dropdownProduct.setOnItemClickListener((parent, view, position, id) -> selectProduct(products.get(position)));
                layoutProduct.setEnabled(!products.isEmpty());
                textViewProductsHint.setText(products.isEmpty()
                        ? getString(R.string.create_no_products_at_branch, fromBranch.getBranchLocation())
                        : getString(R.string.create_products_hint, fromBranch.getBranchLocation()));
            }

            @Override
            public void onError(@NonNull String message, int code) {
                textViewProductsHint.setText(message);
            }
        });
    }

    private void selectProduct(RequestProductFilterDTO product) {
        selectedProduct = product;
        buttonAdd.setEnabled(true);
        setQuantity(1);
        if (isFromWarehouse()) {
            textViewAvailable.setVisibility(View.GONE);
        } else {
            textViewAvailable.setText(getString(R.string.create_available, fromBranch.getBranchLocation(), remainingFor(product)));
            textViewAvailable.setVisibility(View.VISIBLE);
        }
    }

    private int remainingFor(RequestProductFilterDTO product) {
        Line existing = lines.get(product.getProductId());
        return (int) product.getProductQty() - (existing != null ? existing.quantity : 0);
    }

    private int maxQuantity() {
        if (selectedProduct == null) return WAREHOUSE_MAX;
        return isFromWarehouse() ? WAREHOUSE_MAX : Math.max(0, remainingFor(selectedProduct));
    }

    private int quantity() {
        try {
            return editTextQuantity.getText() == null || editTextQuantity.getText().length() == 0
                    ? 0 : Integer.parseInt(editTextQuantity.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setQuantity(int qty) {
        int clamped = Math.max(1, Math.min(Math.max(1, maxQuantity()), qty));
        editTextQuantity.setText(String.valueOf(clamped));
        editTextQuantity.setSelection(editTextQuantity.length());
    }

    private void addProduct() {
        if (selectedProduct == null) return;
        int qty = quantity();
        if (qty <= 0) {
            layoutQuantity.setError(getString(R.string.create_error_quantity));
            return;
        }
        if (!isFromWarehouse() && qty > remainingFor(selectedProduct)) {
            // website: "Exceeds Available Stock"
            layoutQuantity.setError(getString(R.string.create_error_exceeds, Math.max(0, remainingFor(selectedProduct))));
            return;
        }

        Line line = lines.get(selectedProduct.getProductId());
        if (line != null) {
            line.quantity += qty;
        } else {
            lines.put(selectedProduct.getProductId(), new Line(selectedProduct.getProductId(), selectedProduct.getProductName(), qty));
        }

        selectedProduct = null;
        dropdownProduct.setText("", false);
        buttonAdd.setEnabled(false);
        textViewAvailable.setVisibility(View.GONE);
        editTextQuantity.setText("1");
        renderLines();
        refreshSummary();
    }

    private void renderLines() {
        layoutLines.removeAllViews();
        for (Line line : new ArrayList<>(lines.values())) {
            View row = LayoutInflater.from(this).inflate(R.layout.item_request_line, layoutLines, false);
            ((TextView) row.findViewById(R.id.text_view_line_name)).setText(line.name);
            TextView qty = row.findViewById(R.id.text_view_line_quantity);
            qty.setText(getResources().getQuantityString(R.plurals.unit_count, line.quantity, line.quantity));
            StatusStyle.applyTone(qty, StatusStyle.Tone.NEUTRAL, 0);
            row.findViewById(R.id.button_line_remove).setOnClickListener(v -> {
                lines.remove(line.productId);
                renderLines();
                refreshSummary();
            });
            layoutLines.addView(row);
        }
        textViewNoLines.setVisibility(lines.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private int totalUnits() {
        int total = 0;
        for (Line line : lines.values()) total += line.quantity;
        return total;
    }

    private void refreshSummary() {
        int units = totalUnits();
        textViewSummary.setText(getString(R.string.create_summary,
                getResources().getQuantityString(R.plurals.product_count, lines.size(), lines.size()),
                getResources().getQuantityString(R.plurals.unit_count, units, units)));
        String from = fromBranch != null ? fromBranch.getBranchLocation() : "?";
        String to = toBranch != null ? toBranch.getBranchLocation() : "?";
        textViewRoute.setText(getString(R.string.route_format, from, to));
        if (!ButtonLoading.isLoading(buttonSubmit)) {
            buttonSubmit.setEnabled(fromBranch != null && toBranch != null && !lines.isEmpty());
        }
    }

    // ------------------------------------------------------------------ submit

    private void confirmSubmit() {
        if (fromBranch == null || toBranch == null) {
            Toast.makeText(this, R.string.create_error_branches, Toast.LENGTH_LONG).show();
            return;
        }
        if (lines.isEmpty()) {
            Toast.makeText(this, R.string.create_error_no_products, Toast.LENGTH_LONG).show();
            return;
        }
        DialogHelper.confirm(this, getString(R.string.create_confirm_title),
                getString(R.string.create_confirm_message, totalUnits(), lines.size()),
                getString(R.string.create_submit), false, this::submit);
    }

    private void submit() {
        CreateRequestDTO dto = new CreateRequestDTO();
        dto.setFromBranch(fromBranch.getBranchLocation());
        dto.setToBranch(toBranch.getBranchLocation());
        String message = editTextMessage.getText() == null ? "" : editTextMessage.getText().toString().trim();
        dto.setRequestMessage(message.isEmpty() ? null : message);
        List<CreateRequestItemDTO> items = new ArrayList<>();
        for (Line line : lines.values()) items.add(new CreateRequestItemDTO(line.productId, line.quantity));
        dto.setItems(items);

        ButtonLoading.start(buttonSubmit, getString(R.string.sending));
        new RequestRepository(this).createRequest(dto).enqueue(new SafeCallback<CreateRequestResponse>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonSubmit);
            }

            @Override
            public void onSuccess(@Nullable CreateRequestResponse response) {
                Toast.makeText(CreateRequestActivity.this, R.string.toast_request_created, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
