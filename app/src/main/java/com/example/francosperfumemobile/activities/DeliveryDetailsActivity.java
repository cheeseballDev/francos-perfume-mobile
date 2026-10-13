package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.DeliveryItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.ReceiveDeliveryDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.ReceiveDeliveryItemDTO;
import com.example.francosperfumemobile.backend.repository.DeliveryRepository;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.CancelDeliveryResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryDetailResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DispatchDeliveryResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.ReceiveDeliveryResponse;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_DELIVERY_ID = "DELIVERY_ID";
    private TextView textViewDeliveryDetailsId, textViewDeliveryDetailsDateCreated, textViewDeliveryDetailsDirection,
            textViewDeliveryDetailsFromBranch, textViewDeliveryDetailsToBranch, textViewDeliveryDetailsCreatedBy;
    private LinearLayout layoutInboundActions, layoutOutboundActions;
    private MaterialButton buttonConfirmDelivery, buttonCancelRequest, buttonMarkInTransit, buttonGoBack;
    private ProgressBar progressBarDeliveryDetails;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryItemsAdapter deliveryItemsAdapter;
    private String direction;
    private boolean isInbound;
    private final List<DeliveryItemDTO> deliveryItems = new ArrayList<>();

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

                direction = deliveryDetails.getDirection();
                isInbound = "INBOUND".equalsIgnoreCase(direction);
                Log.d("direction", "Fetched direction: '" + direction + "' | Evaluated isInbound: " + isInbound);

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

                String deliveryStatus = response.getData().getDeliveryStatus();
                boolean isCompleted = "COMPLETED".equalsIgnoreCase(deliveryStatus);

                if (isCompleted || "CANCELLED".equalsIgnoreCase(deliveryStatus)) {
                    buttonConfirmDelivery.setVisibility(View.GONE);
                    buttonMarkInTransit.setVisibility(View.GONE);
                    buttonCancelRequest.setVisibility(View.GONE);
                } else {
                    buttonConfirmDelivery.setVisibility(View.VISIBLE);
                    buttonMarkInTransit.setVisibility(View.VISIBLE);
                    buttonCancelRequest.setVisibility(View.VISIBLE);
                }

                if (isInbound) {
                    layoutInboundActions.setVisibility(View.VISIBLE);
                    layoutOutboundActions.setVisibility(View.GONE);
                } else {
                    layoutInboundActions.setVisibility(View.GONE);
                    layoutOutboundActions.setVisibility(View.VISIBLE);
                }

                deliveryItems.clear();
                if (items != null) {
                    deliveryItems.addAll(items);
                }

                deliveryItemsAdapter = new DeliveryItemsAdapter(items, isInbound, isCompleted);
                initializeRecyclerView(items);
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

        deliveryItemsAdapter.setOnItemChangedListener(updatedItem -> {
            boolean found = false;
            for (int i = 0; i < deliveryItems.size(); i++) {
                if (deliveryItems.get(i).getDeliveryItemId() == updatedItem.getDeliveryItemId()) {
                    deliveryItems.set(i, updatedItem);
                    found = true;
                    break;
                }
            }
            if (!found) deliveryItems.add(updatedItem);
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
        if (deliveryItems.isEmpty()) {
            Toast.makeText(this, "No items found to receive", Toast.LENGTH_SHORT).show();
            return;
        }

        int deliveryId = getIntent().getIntExtra(EXTRA_DELIVERY_ID, 0);
        if (deliveryId == 0) {
            Toast.makeText(this, "Invalid Delivery ID", Toast.LENGTH_SHORT).show();
            return;
        }

        List<ReceiveDeliveryItemDTO> receiveItems = new ArrayList<>();
        for (DeliveryItemDTO item : deliveryItems) {
            ReceiveDeliveryItemDTO receiveItem = new ReceiveDeliveryItemDTO();

            receiveItem.setRequestItemId(Integer.parseInt(item.getRequestItemId()));

            receiveItem.setReceivedQty(item.getQuantity());
            receiveItem.setRemarks(null);
            receiveItems.add(receiveItem);
        }

        ReceiveDeliveryDTO dto = new ReceiveDeliveryDTO();
        dto.setExpiryDate("2026-12-31T00:00:00Z");
        dto.setItems(receiveItems);

        DeliveryRepository repository = new DeliveryRepository(this);
        repository.receiveDelivery(deliveryId, dto).enqueue(new SafeCallback<ReceiveDeliveryResponse>(this, progressBarDeliveryDetails) {
            @Override
            public void onSuccess(ReceiveDeliveryResponse response) {
                Toast.makeText(DeliveryDetailsActivity.this, "Delivery received successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void handleCancelRequest() {
        int deliveryId = getIntent().getIntExtra(EXTRA_DELIVERY_ID, 0);
        if (deliveryId == 0) {
            Toast.makeText(this, "Invalid Delivery ID", Toast.LENGTH_SHORT).show();
            return;
        }

        DeliveryRepository repository = new DeliveryRepository(this);
        repository.cancelDelivery(deliveryId).enqueue(new SafeCallback<CancelDeliveryResponse>(this, progressBarDeliveryDetails) {
            @Override
            public void onSuccess(CancelDeliveryResponse response) {
                Toast.makeText(DeliveryDetailsActivity.this, "Delivery cancelled successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void handleMarkInTransit() {
        int deliveryId = getIntent().getIntExtra(EXTRA_DELIVERY_ID, 0);
        if (deliveryId == 0) {
            Toast.makeText(this, "Invalid Delivery ID", Toast.LENGTH_SHORT).show();
            return;
        }

        DeliveryRepository repository = new DeliveryRepository(this);
        repository.dispatchDelivery(deliveryId).enqueue(new SafeCallback<DispatchDeliveryResponse>(this, progressBarDeliveryDetails) {
            @Override
            public void onSuccess(DispatchDeliveryResponse response) {
                Toast.makeText(DeliveryDetailsActivity.this, "Delivery marked as in transit", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}