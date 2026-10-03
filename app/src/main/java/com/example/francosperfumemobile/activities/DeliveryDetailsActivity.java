package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.DeliveryItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDetailsDTO;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDetailsActivity extends AppCompatActivity {


    private final List<DisplayDeliveryDetailsDTO> deliveryDetailsList = new ArrayList<>();
    private final List<DeliveryItemDTO> deliveryItemList = new ArrayList<>();
    private TextView textViewDeliveryDetailsId, textViewDeliveryDetailsDateCreated, textViewDeliveryDetailsDirection,
            textViewDeliveryDetailsFromBranch, textViewDeliveryDetailsToBranch, textViewDeliveryDetailsCreatedBy;
    private ProgressBar progressBarDeliveryDetails;
    private AppCompatButton buttonAccept, buttonReject;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryItemsAdapter deliveryItemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout_inventory_batch_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeRecyclerView();

    }

    private void initializeUI() {
        textViewDeliveryDetailsId = findViewById(R.id.text_view_delivery_details_id);
        textViewDeliveryDetailsDateCreated = findViewById(R.id.text_view_request_details_date_submitted);
        textViewDeliveryDetailsDirection = findViewById(R.id.text_view_delivery_direction);
        textViewDeliveryDetailsFromBranch = findViewById(R.id.text_view_delivery_from_branch);
        textViewDeliveryDetailsToBranch = findViewById(R.id.text_view_delivery_to_branch);
        textViewDeliveryDetailsCreatedBy = findViewById(R.id.text_view_delivery_details_created_by);

        progressBarDeliveryDetails = findViewById(R.id.progress_bar_delivery_details);

        buttonAccept = findViewById(R.id.button_accept);
        buttonReject = findViewById(R.id.button_reject_cancel);
    }

    private void initializeRecyclerView() {
        recyclerViewDeliveryItems = findViewById(R.id.recycler_view_delivery_items);
        deliveryItemsAdapter = new DeliveryItemsAdapter(deliveryItemList, selectedDelivery -> {

        });
        recyclerViewDeliveryItems.setAdapter(deliveryItemsAdapter);
        RecyclerViewHelper.setupVertical(this, recyclerViewDeliveryItems, deliveryItemsAdapter);
    }
}