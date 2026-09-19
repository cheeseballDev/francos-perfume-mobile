package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.BatchAdapter;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryBatchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.dialogs.EditBatchDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class InventoryBatchListActivity extends AppCompatActivity {

    private MaterialButton backButton;
    private final List<DisplayBatchDTO> batchList = new ArrayList<>();
    private static final String PRODUCT_ID = "PRODUCT_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory_batch_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null && getIntent().hasExtra(PRODUCT_ID)) {
            int productId = getIntent().getIntExtra(PRODUCT_ID, -1);
            // TODO: create functions to add all batches with X productId into the recyclerview. or change the logic i dont mind
        }

        initializeUI();
        initializeRecyclerView();
    }

    public static Intent newIntent(Context context, int productId) {
        Intent intent = new Intent(context, InventoryBatchListActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }



    private void initializeRecyclerView() {
        BatchAdapter adapter = new BatchAdapter(batchList, selectedBatch -> {
            EditBatchDialog dialog = EditBatchDialog.newInstance(selectedBatch);
            dialog.show(getSupportFragmentManager(), "EditBatchDialog");
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view_batch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initializeUI() {
        backButton = findViewById(R.id.button_go_back);
        intializeListeners();
    }

    private void intializeListeners() {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("PREVIOUS_ACTIVE_FRAGMENT", "INVENTORY");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}