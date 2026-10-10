package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryFilterDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDetailsDTO;
import com.example.francosperfumemobile.backend.repository.DeliveryRepository;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryDetailResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryListResponse;
import com.example.francosperfumemobile.helpers.PaginationHelper;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDetailsActivity extends AppCompatActivity {


    private final List<DisplayDeliveryDetailsDTO> deliveryDetailsList = new ArrayList<>();
    private static final String EXTRA_DELIVERY_ID = "DELIVERY_ID";
    private final List<DeliveryItemDTO> deliveryItemList = new ArrayList<>();
    private TextView textViewDeliveryDetailsId, textViewDeliveryDetailsDateCreated, textViewDeliveryDetailsDirection,
            textViewDeliveryDetailsFromBranch, textViewDeliveryDetailsToBranch, textViewDeliveryDetailsCreatedBy;
    private LinearLayout layoutInboundActions, layoutOutboundActions;
    private MaterialButton buttonConfirmDelivery, buttonCancelRequest, buttonMarkInTransit, buttonGoBack;
    private ProgressBar progressBarDeliveryDetails;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryItemsAdapter deliveryItemsAdapter;
    private String direction;
    private boolean isInbound;
    private final List<DeliveryItemDTO> updatedItems = new ArrayList<>();


    public static Intent newIntent(Context context, int deliveryId) {
        Intent intent = new Intent(context, DeliveryDetailsActivity.class);
        intent.putExtra(EXTRA_DELIVERY_ID, deliveryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout_delivery_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //todo: add intent here
        // also, can add intent that gets the direction type so that it doesn't have to load the delivery details just to get the delivery type
        // but idk
        int deliveryId = getIntent().getIntExtra(EXTRA_DELIVERY_ID, 0);
        initializeUI();
        fetchDeliveryDetails(deliveryId);
    }
    private void fetchDeliveryDetails(int deliveryId) {
        DeliveryRepository repository = new DeliveryRepository(this);
        repository.getDeliveryDetails(deliveryId).enqueue(new SafeCallback<DeliveryDetailResponse>(this, progressBarDeliveryDetails) {
            @Override
            public void onSuccess(DeliveryDetailResponse response) {
                DisplayDeliveryDetailsDTO deliveryDetails = response.getData();
                List<DeliveryItemDTO> items = response.getData().getItems();

                initializeRecyclerView(items);

                direction = deliveryDetails.getDirection();
                isInbound = "INBOUND".equalsIgnoreCase(direction);

                if (items != null && !items.isEmpty()) {
                    deliveryItemsAdapter.updateData(items);
                }

                String rawDate = response.getData().getCreatedAt();
                String dateOnly = (rawDate != null && rawDate.contains("T"))
                        ? rawDate.split("T")[0]
                        : rawDate;

                textViewDeliveryDetailsId.setText(deliveryDetails.getDeliveryDisplayId());
                textViewDeliveryDetailsCreatedBy.setText(deliveryDetails.getCreatedBy());
                textViewDeliveryDetailsDateCreated.setText(dateOnly);
                textViewDeliveryDetailsFromBranch.setText(deliveryDetails.getFromBranchName());
                textViewDeliveryDetailsToBranch.setText(deliveryDetails.getToBranchName());
                textViewDeliveryDetailsDirection.setText(direction);

                if (isInbound) {
                    layoutInboundActions.setVisibility(View.VISIBLE);
                    layoutOutboundActions.setVisibility(View.GONE);
                } else {
                    layoutInboundActions.setVisibility(View.GONE);
                    layoutOutboundActions.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void initializeUI() {
        textViewDeliveryDetailsId = findViewById(R.id.text_view_delivery_details_id);
        textViewDeliveryDetailsDateCreated = findViewById(R.id.text_view_delivery_details_date_submitted);
        textViewDeliveryDetailsDirection = findViewById(R.id.text_view_delivery_details_direction);
        textViewDeliveryDetailsFromBranch = findViewById(R.id.text_view_delivery_details_from_branch);
        textViewDeliveryDetailsToBranch = findViewById(R.id.text_view_delivery_details_to_branch);
        textViewDeliveryDetailsCreatedBy = findViewById(R.id.text_view_delivery_details_created_by);

        progressBarDeliveryDetails = findViewById(R.id.progress_bar_delivery_details);

        layoutInboundActions = findViewById(R.id.layout_inbound_actions);
        layoutOutboundActions = findViewById(R.id.layout_outbound_actions);

        buttonConfirmDelivery = findViewById(R.id.button_confirm_delivery);
        buttonCancelRequest = findViewById(R.id.button_cancel_request);
        buttonMarkInTransit = findViewById(R.id.button_mark_in_transit);
        buttonGoBack = findViewById(R.id.button_delivery_details_go_back);

        initializeListeners();
    }

    private void initializeRecyclerView(List<DeliveryItemDTO> items) {
        recyclerViewDeliveryItems = findViewById(R.id.recycler_view_delivery_items);

        deliveryItemsAdapter = new DeliveryItemsAdapter(items, isInbound);

        // this one is AI generated but basically it gets the updated items from the adapter and adds it into the list here in the activity
        // and you use that list to push into the backend (I THINK)
        // listen for item changes
        deliveryItemsAdapter.setOnItemChangedListener(updatedItem -> {
            // update or add to updatedItems list
            boolean found = false;
            for (int i = 0; i < updatedItems.size(); i++) {
                if (updatedItems.get(i).getDeliveryItemId() == updatedItem.getDeliveryItemId()) {
                    updatedItems.set(i, updatedItem);
                    found = true;
                    break;
                }
            }
            if (!found) updatedItems.add(updatedItem);
        });

        recyclerViewDeliveryItems.setAdapter(deliveryItemsAdapter);
        RecyclerViewHelper.setupVertical(this, recyclerViewDeliveryItems, deliveryItemsAdapter);
    }



    private void initializeListeners() {
        buttonConfirmDelivery.setOnClickListener(v -> handleConfirmDelivery());
        buttonCancelRequest.setOnClickListener(v -> handleCancelRequest());
        buttonMarkInTransit.setOnClickListener(v -> handleMarkInTransit());
        buttonGoBack.setOnClickListener(v -> finish());
    }


    private void handleConfirmDelivery() {
        // this is if the updated items are empty to give you an idea
        if (updatedItems.isEmpty()) {
            return;
        }

    }

    private void handleCancelRequest() {

    }

    private void handleMarkInTransit() {

    }
}