package com.example.francosperfumemobile.activities;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class DeliveryDetailsActivity extends AppCompatActivity {


    private final List<DisplayDeliveryDetailsDTO> deliveryDetailsList = new ArrayList<>();
    private static final String EXTRA_DELIVERY_ID = "DELIVERY_ID";
    private final List<DeliveryItemDTO> deliveryItemList = new ArrayList<>();
    private TextView textViewDeliveryDetailsId, textViewDeliveryDetailsDateCreated, textViewDeliveryDetailsDirection,
            textViewDeliveryDetailsFromBranch, textViewDeliveryDetailsToBranch, textViewDeliveryDetailsCreatedBy;
    private ProgressBar progressBarDeliveryDetails;
    private AppCompatButton buttonAccept, buttonReject;
    private RecyclerView recyclerViewDeliveryItems;
    private DeliveryItemsAdapter deliveryItemsAdapter;
    private String direction;


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
        initializeRecyclerView();
        fetchDeliveryDetails(deliveryId);
    }

    private void fetchDeliveryDetails(int deliveryId) {
        DeliveryRepository repository = new DeliveryRepository(this);
        repository.getDeliveryDetails(deliveryId).enqueue(new SafeCallback<DeliveryDetailResponse>(this, progressBarDeliveryDetails) {
            @Override
            public void onSuccess(DeliveryDetailResponse response) {
                List<DeliveryItemDTO> items = response.getData().getItems();

                if (items != null && !items.isEmpty()) {
                    deliveryItemsAdapter.updateData(items);
                }

                String rawDate = response.getData().getCreatedAt();
                String dateOnly = (rawDate != null && rawDate.contains("T"))
                        ? rawDate.split("T")[0]
                        : rawDate;

                textViewDeliveryDetailsId.setText(response.getData().getDeliveryDisplayId());
                textViewDeliveryDetailsCreatedBy.setText(response.getData().getCreatedBy());
                textViewDeliveryDetailsDateCreated.setText(dateOnly);
                textViewDeliveryDetailsFromBranch.setText(response.getData().getFromBranchName());
                textViewDeliveryDetailsToBranch.setText(response.getData().getToBranchName());
                // todo: add formatting here in the future
                textViewDeliveryDetailsDirection.setText(response.getData().getDirection());

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

        buttonAccept = findViewById(R.id.button_accept);
        buttonReject = findViewById(R.id.button_reject_cancel);
        //initializeListeners();
    }

    private void initializeRecyclerView() {
        recyclerViewDeliveryItems = findViewById(R.id.recycler_view_delivery_items);
        deliveryItemsAdapter = new DeliveryItemsAdapter(deliveryItemList, direction, selectedDelivery -> {

        });
        recyclerViewDeliveryItems.setAdapter(deliveryItemsAdapter);
        RecyclerViewHelper.setupVertical(this, recyclerViewDeliveryItems, deliveryItemsAdapter);
    }


    /*
    private void initializeListeners() {

        //todo: add if statement to add if conditions that changes the button layout depending on the direction
        buttonAccept.setOnClickListener(v -> {

        });

        buttonReject.setOnClickListener(v -> {

        });
    }

     */
}