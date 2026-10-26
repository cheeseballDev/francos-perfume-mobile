package com.example.francosperfumemobile.dialogs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.BundleCompat;
import androidx.fragment.app.DialogFragment;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.EditBatchDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.DialogHelper;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Edit a batch's expiry date and quantity (website: EditBatchModal).
 * Saves with PATCH api/Inventory/batch/{batchId}; the screen behind reloads through
 * the fragment result {@link #RESULT_KEY}.
 */
public class EditBatchDialog extends DialogFragment {

    public static final String RESULT_KEY = "edit_batch_saved";
    private static final String ARG_BATCH = "BATCH_ITEM";
    private static final String STATE_EXPIRY = "expiry";

    /** Same choices as the website. */
    private static final List<String> REASONS = Arrays.asList("Restock", "Correction", "Damaged", "Expired");
    /** Website limit for the stepper; a batch that already holds more keeps its own size as the limit. */
    private static final int WEBSITE_MAX_QUANTITY = 99;

    private DisplayBatchDTO batch;
    private String expiryDay; // yyyy-MM-dd
    private String reason;
    private int maxQuantity;
    private boolean dirty;

    private TextInputEditText editTextTargetDate, editTextBatchStock;
    private TextInputLayout layoutTargetDate, layoutStock;
    private MaterialAutoCompleteTextView dropdownReason;
    private TextView textViewError;
    private MaterialButton buttonSave;

    public static EditBatchDialog newInstance(DisplayBatchDTO selectedBatch) {
        EditBatchDialog dialog = new EditBatchDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BATCH, selectedBatch);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_FP_Dialog);
        if (getArguments() != null) {
            batch = BundleCompat.getParcelable(getArguments(), ARG_BATCH, DisplayBatchDTO.class);
        }
        if (savedInstanceState != null) expiryDay = savedInstanceState.getString(STATE_EXPIRY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit_batch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (batch == null) {
            dismiss();
            return;
        }

        // Bind every view first (they were never bound before, so opening the dialog crashed)
        TextView textViewSelectedBatch = view.findViewById(R.id.text_view_batch);
        TextView textViewSelectedProduct = view.findViewById(R.id.text_view_batch_selected_product_name);
        TextView textViewDateReceived = view.findViewById(R.id.text_view_batch_date_received_dialog);
        editTextTargetDate = view.findViewById(R.id.edit_text_batch_target_date);
        layoutTargetDate = view.findViewById(R.id.text_input_layout_batch_target_date);
        editTextBatchStock = view.findViewById(R.id.edit_text_batch_stock);
        layoutStock = view.findViewById(R.id.text_input_layout_batch_stock);
        dropdownReason = view.findViewById(R.id.dropdown_reason);
        textViewError = view.findViewById(R.id.text_view_edit_batch_error);
        buttonSave = view.findViewById(R.id.button_save_edit_batch);

        textViewSelectedBatch.setText(getString(R.string.edit_batch_title, batch.getBatchDisplayId()));
        String product = batch.getProductName() != null ? batch.getProductName() : "";
        if (batch.getBranchLocation() != null) product += " · " + batch.getBranchLocation();
        textViewSelectedProduct.setText(product);
        textViewDateReceived.setText(getString(R.string.received_on, DateUtils.formatDate(batch.getCreatedAt())));

        maxQuantity = Math.max(WEBSITE_MAX_QUANTITY, batch.getQuantity());
        if (expiryDay == null) expiryDay = toDay(batch.getExpiryDate());
        showExpiry();
        editTextBatchStock.setText(String.valueOf(batch.getQuantity()));

        dropdownReason.setAdapter(new FilterManager.NoFilterAdapter(requireContext(), REASONS));
        dropdownReason.setOnItemClickListener((parent, v, position, id) -> {
            reason = REASONS.get(position);
            dirty = true;
            textViewError.setVisibility(View.GONE);
        });

        editTextTargetDate.setOnClickListener(v -> pickDate());
        layoutTargetDate.setEndIconOnClickListener(v -> pickDate());
        view.findViewById(R.id.button_minus).setOnClickListener(v -> setQuantity(currentQuantity() - 1));
        view.findViewById(R.id.button_plus).setOnClickListener(v -> setQuantity(currentQuantity() + 1));
        editTextBatchStock.addTextChangedListener(new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                dirty = true;
                int qty = currentQuantity();
                layoutStock.setError(qty > maxQuantity ? getString(R.string.error_max_quantity, maxQuantity) : null);
            }
        });

        view.findViewById(R.id.button_close).setOnClickListener(v -> closeAsking());
        view.findViewById(R.id.button_cancel_edit_batch).setOnClickListener(v -> closeAsking());
        buttonSave.setOnClickListener(v -> save());
        dirty = false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_EXPIRY, expiryDay);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    // ------------------------------------------------------------------ fields

    private int currentQuantity() {
        try {
            return editTextBatchStock.getText() == null || editTextBatchStock.getText().length() == 0
                    ? 0 : Integer.parseInt(editTextBatchStock.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setQuantity(int qty) {
        int clamped = Math.max(0, Math.min(maxQuantity, qty));
        editTextBatchStock.setText(String.valueOf(clamped));
        editTextBatchStock.setSelection(editTextBatchStock.length());
    }

    private void showExpiry() {
        editTextTargetDate.setText(expiryDay == null ? "" : DateUtils.formatDate(expiryDay));
        layoutTargetDate.setError(null);
    }

    private void pickDate() {
        Calendar initial = expiryDay != null ? DateUtils.toCalendar(expiryDay) : null;
        if (initial == null) initial = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(requireContext(), (p, year, month, day) -> {
            expiryDay = DateUtils.toIsoDay(year, month, day);
            dirty = true;
            showExpiry();
        }, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH));
        // Website: the target date can't be earlier than the date received
        Date received = DateUtils.parse(batch.getCreatedAt());
        if (received != null) picker.getDatePicker().setMinDate(received.getTime());
        picker.show();
    }

    @Nullable
    private static String toDay(@Nullable String iso) {
        Calendar c = DateUtils.toCalendar(iso);
        return c == null ? null : DateUtils.toIsoDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private void closeAsking() {
        if (!dirty) {
            dismiss();
            return;
        }
        // Website: "Discard changes?" before closing an edited modal
        DialogHelper.confirm(requireContext(), getString(R.string.discard_title), getString(R.string.discard_message),
                getString(R.string.discard), true, this::dismiss);
    }

    // -------------------------------------------------------------------- save

    private void save() {
        if (ButtonLoading.isLoading(buttonSave)) return;
        int qty = currentQuantity();

        if (expiryDay == null) {
            layoutTargetDate.setError(getString(R.string.error_expiry_required));
            return;
        }
        Date received = DateUtils.parse(batch.getCreatedAt());
        Date expiry = DateUtils.parse(expiryDay);
        if (received != null && expiry != null && expiry.before(DateUtils.parse(DateUtils.formatIsoDay(received)))) {
            layoutTargetDate.setError(getString(R.string.error_expiry_before_received, DateUtils.formatDate(batch.getCreatedAt())));
            return;
        }
        if (qty > maxQuantity) {
            layoutStock.setError(getString(R.string.error_max_quantity, maxQuantity));
            return;
        }
        if (reason == null) {
            textViewError.setText(R.string.error_reason_required);
            textViewError.setVisibility(View.VISIBLE);
            return;
        }

        EditBatchDTO dto = new EditBatchDTO();
        dto.setProductId(batch.getProductId());
        dto.setQuantity(qty);
        dto.setExpiryDate(expiryDay);
        dto.setReason(reason);

        ButtonLoading.start(buttonSave, getString(R.string.saving));
        new InventoryRepository(requireContext()).updateInventoryBatch(batch.getBatchId(), dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!isAdded()) return;
                ButtonLoading.stop(buttonSave);
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), R.string.toast_batch_updated, Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().setFragmentResult(RESULT_KEY, new Bundle());
                    dismiss();
                } else {
                    showError(com.example.francosperfumemobile.helpers.ApiErrorParser.fromResponse(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                ButtonLoading.stop(buttonSave);
                showError(com.example.francosperfumemobile.helpers.ApiErrorParser.fromThrowable(t));
            }
        });
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
    }
}
