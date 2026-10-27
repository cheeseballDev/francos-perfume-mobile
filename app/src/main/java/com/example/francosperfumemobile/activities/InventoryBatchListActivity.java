package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.BatchAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.SendInventoryBatchRequestDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.BatchResponse;
import com.example.francosperfumemobile.dialogs.EditBatchDialog;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.example.francosperfumemobile.helpers.StatusStyle;

import java.util.ArrayList;
import java.util.List;

public class InventoryBatchListActivity extends BaseActivity {

    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String BRANCH_ID = "BRANCH_ID";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String PRODUCT_DISPLAY_ID = "PRODUCT_DISPLAY_ID";
    private static final String BRANCH_NAME = "BRANCH_NAME";
    private static final String IMAGE_URL = "IMAGE_URL";

    private int productId, branchId;
    private String productName, productDisplayId, branchName, imageUrl;

    private BatchAdapter batchAdapter;
    private HeaderAdapter headerAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private View progress, emptyState, topBar;

    public static Intent newIntent(Context context, int productId) {
        Intent intent = new Intent(context, InventoryBatchListActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }
    public static Intent newIntent(Context context, DisplayInventoryDTO product) {
        Intent intent = newIntent(context, product.getProductId());
        if (product.getBranchId() != null) intent.putExtra(BRANCH_ID, product.getBranchId().intValue());
        intent.putExtra(PRODUCT_NAME, product.getProductName());
        intent.putExtra(PRODUCT_DISPLAY_ID, product.getProductDisplayId());
        intent.putExtra(BRANCH_NAME, product.getBranchName());
        intent.putExtra(IMAGE_URL, product.getProductImageUrl());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) return;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory_batch_list);

        Intent intent = getIntent();
        productId = intent.getIntExtra(PRODUCT_ID, -1);
        if (productId == -1) {
            Toast.makeText(this, R.string.error_unknown_product, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        branchId = intent.getIntExtra(BRANCH_ID, sessionManager.getBranchId());
        productName = intent.getStringExtra(PRODUCT_NAME);
        productDisplayId = intent.getStringExtra(PRODUCT_DISPLAY_ID);
        branchName = intent.getStringExtra(BRANCH_NAME);
        if (branchName == null && !intent.hasExtra(BRANCH_ID)) branchName = sessionManager.getBranchLocation();
        imageUrl = intent.getStringExtra(IMAGE_URL);

        View root = findViewById(R.id.drawerlayout_inventory_batch_list);
        topBar = findViewById(R.id.top_navigation_bar_inventory_batch);
        InsetsHelper.applyEdgeToEdge(root, topBar, null, false);
        setupDetailTopBar(topBar, productName != null ? productName : getString(R.string.batches_title), branchName);

        swipeRefresh = findViewById(R.id.swipe_refresh_batches);
        swipeRefresh.setColorSchemeResources(R.color.fp_foreground);
        swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.fp_card);
        swipeRefresh.setOnRefreshListener(() -> loadBatches(false));
        progress = findViewById(R.id.progress_bar_batches);
        emptyState = findViewById(R.id.layout_empty_batches);
        ((ImageView) emptyState.findViewById(R.id.image_view_empty_icon)).setImageResource(R.drawable.ic_package);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_batch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headerAdapter = new HeaderAdapter();
        batchAdapter = new BatchAdapter(new ArrayList<>(), this::openEditBatch);
        recyclerView.setAdapter(new ConcatAdapter(headerAdapter, batchAdapter));

        // Reload after a batch was saved in the dialog
        getSupportFragmentManager().setFragmentResultListener(EditBatchDialog.RESULT_KEY, this, (key, result) -> {
            setResult(RESULT_OK);
            loadBatches(false);
        });

        loadBatches(true);
    }

    private void openEditBatch(DisplayBatchDTO batch) {
        if (getSupportFragmentManager().findFragmentByTag("EditBatchDialog") != null) return;
        if (batch.getBranchLocation() == null && branchName != null) batch.setBranchLocation(branchName);
        EditBatchDialog.newInstance(batch).show(getSupportFragmentManager(), "EditBatchDialog");
    }

    private void loadBatches(boolean firstLoad) {
        if (firstLoad) progress.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);

        SendInventoryBatchRequestDTO dto = new SendInventoryBatchRequestDTO();
        dto.setProductId(productId);
        dto.setBranchId(branchId);

        new InventoryRepository(this).getInventoryBatches(dto).enqueue(new SafeCallback<BatchResponse>(this) {
            @Override
            public void onFinish() {
                progress.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(@Nullable BatchResponse response) {
                List<DisplayBatchDTO> batches = response != null && response.getBatches() != null
                        ? response.getBatches() : new ArrayList<>();
                if (productName == null && !batches.isEmpty()) {
                    productName = batches.get(0).getProductName();
                    setDetailTitle(topBar, productName, branchName);
                }
                int total = response != null ? response.getInventoryTotal() : 0;
                if (total == 0) for (DisplayBatchDTO b : batches) total += b.getQuantity();
                headerAdapter.update(total, batches.size());
                batchAdapter.updateData(batches);

                if (batches.isEmpty()) {
                    ((TextView) emptyState.findViewById(R.id.text_view_empty_title)).setText(R.string.no_batches_title);
                    ((TextView) emptyState.findViewById(R.id.text_view_empty_message)).setText(R.string.no_batches_message);
                    emptyState.findViewById(R.id.button_empty_retry).setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(@NonNull String message, int code) {
                if (code == 401) return;
                if (batchAdapter.getItemCount() > 0) {
                    super.onError(message, code);
                    return;
                }
                ((TextView) emptyState.findViewById(R.id.text_view_empty_title)).setText(R.string.error_load_title);
                ((TextView) emptyState.findViewById(R.id.text_view_empty_message)).setText(message);
                View retry = emptyState.findViewById(R.id.button_empty_retry);
                retry.setVisibility(View.VISIBLE);
                retry.setOnClickListener(v -> loadBatches(true));
                emptyState.setVisibility(View.VISIBLE);
            }
        });
    }

    private class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int totalUnits = -1;
        private int batchCount = -1;

        void update(int totalUnits, int batchCount) {
            this.totalUnits = totalUnits;
            this.batchCount = batchCount;
            notifyItemChanged(0);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_batch_list, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            View v = holder.itemView;
            ((TextView) v.findViewById(R.id.text_view_product_name)).setText(productName != null ? productName : "—");
            TextView idView = v.findViewById(R.id.text_view_batch_product_id);
            idView.setText(productDisplayId);
            idView.setVisibility(productDisplayId == null ? View.GONE : View.VISIBLE);

            TextView branch = v.findViewById(R.id.text_view_batch_branch);
            if (branchName != null) {
                branch.setText(branchName);
                StatusStyle.applyTone(branch, "WAREHOUSE".equalsIgnoreCase(branchName)
                        ? StatusStyle.Tone.PENDING : StatusStyle.Tone.SUCCESS, R.drawable.ic_map_pin);
                branch.setVisibility(View.VISIBLE);
            } else {
                branch.setVisibility(View.GONE);
            }

            View units = v.findViewById(R.id.info_batch_total_units);
            ((TextView) units.findViewById(R.id.text_view_info_label)).setText(R.string.total_units_label);
            ((TextView) units.findViewById(R.id.text_view_info_value)).setText(totalUnits < 0 ? "—" : String.valueOf(totalUnits));
            View count = v.findViewById(R.id.info_batch_count);
            ((TextView) count.findViewById(R.id.text_view_info_label)).setText(R.string.batches_title);
            ((TextView) count.findViewById(R.id.text_view_info_value)).setText(batchCount < 0 ? "—" : String.valueOf(batchCount));

            ImageView image = v.findViewById(R.id.image_view_batch_product);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(image).load(imageUrl).placeholder(R.drawable.francos_perfume_logo)
                        .error(R.drawable.francos_perfume_logo).centerCrop().into(image);
            } else {
                image.setImageResource(R.drawable.francos_perfume_logo);
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
