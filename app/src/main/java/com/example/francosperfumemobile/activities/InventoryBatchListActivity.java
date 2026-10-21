package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.BatchAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.SendInventoryBatchRequestDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.BatchResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.example.francosperfumemobile.helpers.NavigationHelper;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class InventoryBatchListActivity extends BaseActivity {

    private MaterialButton backButton;
    private final List<DisplayBatchDTO> batchList = new ArrayList<>();
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private DrawerLayout drawerLayout;
    private View topToolbar;
    private TextView productName;
    private ImageButton buttonMenu, buttonNotification;
    private AppCompatButton buttonLogout;
    private InventoryRepository inventoryRepository;
    private BatchAdapter batchAdapter;

    public static Intent newIntent(Context context, int productId) {
        Intent intent = new Intent(context, InventoryBatchListActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory_batch_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout_inventory_batch_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeRecyclerView();
        intializeListeners();

        if (getIntent() != null && getIntent().hasExtra(PRODUCT_ID)) {
            int productId = getIntent().getIntExtra(PRODUCT_ID, -1);

            if (productId == -1) {
                Toast.makeText(this, "Unknown product", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            int branchId = new SessionManager(this).getBranchId();

            SendInventoryBatchRequestDTO dto = new SendInventoryBatchRequestDTO();
            dto.setProductId(productId);
            dto.setBranchId(branchId);
            inventoryRepository = new InventoryRepository(this);
            loadBatches(dto);
        }
    }

    private void initializeUI() {
        backButton = findViewById(R.id.button_inventory_batch_go_back);
        topToolbar = findViewById(R.id.top_navigation_bar_inventory_batch);
        buttonMenu = topToolbar.findViewById(R.id.button_menu);
        buttonNotification = topToolbar.findViewById(R.id.button_notification);
        productName = findViewById(R.id.text_view_product_name);

        drawerLayout = findViewById(R.id.drawerlayout_inventory_batch_list);
        setupDrawer(drawerLayout);

    }

    //selectedBatch -> {
    //            EditBatchDialog dialog = EditBatchDialog.newInstance(selectedBatch);
    //            dialog.show(getSupportFragmentManager(), "EditBatchDialog");
    //        }
    private void initializeRecyclerView() {
        RecyclerView recyclerViewBatch = findViewById(R.id.recycler_view_batch);
        batchAdapter = new BatchAdapter(batchList);
        RecyclerViewHelper.setupVertical(this, recyclerViewBatch, batchAdapter);
    }


    private void intializeListeners() {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("PREVIOUS_ACTIVE_FRAGMENT", "INVENTORY");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        buttonMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        buttonNotification.setOnClickListener(v -> {
            Toast.makeText(this, "To be implemented", Toast.LENGTH_SHORT).show();
        });

        /*
        buttonLogout.setOnClickListener(v -> {
            SessionManager sm = new SessionManager(this);
            sm.clearSession();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

         */
        NavigationHelper.setupDrawerBackButton(this, drawerLayout);
    }

    private void loadBatches(SendInventoryBatchRequestDTO dto) {
        inventoryRepository.getInventoryBatches(dto).enqueue(new SafeCallback<BatchResponse>(this) {
            @Override
            public void onSuccess(BatchResponse batchResponse) {
                List<DisplayBatchDTO> batches = batchResponse.getBatches();

                batchList.clear();
                if (batches != null && !batches.isEmpty()) {
                    String name = batches.get(0).getProductName();
                    if (name != null) {
                        productName.setText(name);
                    }
                    batchList.addAll(batches);
                } else {
                    productName.setText("No Batches Available");
                    Toast.makeText(InventoryBatchListActivity.this, "No batches found", Toast.LENGTH_SHORT).show();
                }
                batchAdapter.notifyDataSetChanged();
            }
        });
    }
}

