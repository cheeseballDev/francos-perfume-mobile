package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.francosperfumemobile.adapters.RequestApprovalAdapter;
import com.example.francosperfumemobile.adapters.RequestedItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestDetailResponse;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_REQUEST_ID = "REQUEST_ID";

    private final List<DisplayRequestItemDetailsDTO> listRequestedProducts = new ArrayList<>();
    private final List<DisplayRequestApprovalDetailsDTO> listRequestTimeline = new ArrayList<>();

    private TextView textViewRequestId, textViewDateSubmitted, textViewDirection,
            textViewFromBranch, textViewToBranch, textViewCreatedBy;
    private ProgressBar progressBarMain;

    private RequestApprovalAdapter requestApprovalAdapter;
    private RequestedItemsAdapter requestedItemsAdapter;

    public static Intent newIntent(Context context, int requestId) {
        Intent intent = new Intent(context, RequestDetailsActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout_request_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int requestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        if (requestId == -1) {
            Toast.makeText(this, "Invalid request ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeUI();
        initializeRecyclerViews();
        initializeListeners();
        fetchRequestDetails(requestId);

    }
    private void fetchRequestDetails(int requestId) {
        RequestRepository repository = new RequestRepository(this);
        repository.getRequestDetails(requestId).enqueue(new SafeCallback<RequestDetailResponse>(this, progressBarMain) {
            @Override
            public void onSuccess(RequestDetailResponse detailResponse) {
                if (detailResponse.getData() == null) {
                    Toast.makeText(RequestDetailsActivity.this, "Request details not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                String rawDate = detailResponse.getData().getRequestDateSubmitted();
                String dateOnly = (rawDate != null && rawDate.contains("T"))
                        ? rawDate.split("T")[0]
                        : rawDate;
                String requestStatus = detailResponse.getData().getRequestStatus();
                if(requestStatus.equals("COMPLETED")){
                    //TODO: add condition here that hides those yee yee ass buttons
                    //TODO: also leave the checkmark permanently checked and approved qty grayed out
                }

                textViewDateSubmitted.setText(String.format("Date submitted: %s", dateOnly));
                textViewRequestId.setText(detailResponse.getData().getRequestDisplayId());
                textViewFromBranch.setText(detailResponse.getData().getRequestedFrom());
                textViewToBranch.setText(detailResponse.getData().getDeliveredTo());
                textViewCreatedBy.setText(detailResponse.getData().getEmployeeName());
                textViewDirection.setText(detailResponse.getData().getRequestDirection());

                if (detailResponse.getData().getItems() != null) {
                    listRequestedProducts.clear();
                    listRequestedProducts.addAll(detailResponse.getData().getItems());
                    requestedItemsAdapter.notifyDataSetChanged();
                }

                if (detailResponse.getData().getApprovals() != null) {
                    listRequestTimeline.clear();
                    listRequestTimeline.addAll(detailResponse.getData().getApprovals());
                    requestApprovalAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initializeUI() {
        textViewRequestId = findViewById(R.id.text_view_request_details_id);
        textViewDateSubmitted = findViewById(R.id.text_view_request_details_date_submitted);
        textViewDirection = findViewById(R.id.text_view_request_details_direction);
        textViewFromBranch = findViewById(R.id.text_view_request_details_from_branch);
        textViewToBranch = findViewById(R.id.text_view_request_details_to_branch);
        textViewCreatedBy = findViewById(R.id.text_view_request_details_created_by);

        progressBarMain = findViewById(R.id.progress_bar_main);
    }

    private void initializeRecyclerViews() {
        RecyclerView requestedProductsRecyclerView = findViewById(R.id.recycler_view_requested_products);
        RecyclerView requestApprovalRecyclerView = findViewById(R.id.recycler_view_request_timeline);

        requestedItemsAdapter = new RequestedItemsAdapter(listRequestedProducts);
        requestApprovalAdapter = new RequestApprovalAdapter(listRequestTimeline);

        RecyclerViewHelper.setupVertical(this, requestedProductsRecyclerView, requestedItemsAdapter);
        RecyclerViewHelper.setupVertical(this, requestApprovalRecyclerView, requestApprovalAdapter);
    }

    private void initializeListeners() {
        findViewById(R.id.button_request_details_go_back).setOnClickListener(v -> finish());
    }

}