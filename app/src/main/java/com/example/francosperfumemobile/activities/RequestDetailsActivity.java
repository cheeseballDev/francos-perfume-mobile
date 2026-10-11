package com.example.francosperfumemobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.RequestApprovalAdapter;
import com.example.francosperfumemobile.adapters.RequestedItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.ApproveRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RejectRequestDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.ApprovalResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RejectResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestDetailResponse;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_REQUEST_ID = "REQUEST_ID";

    private final List<DisplayRequestItemDetailsDTO> listRequestedProducts = new ArrayList<>();
    private final List<DisplayRequestApprovalDetailsDTO> listRequestTimeline = new ArrayList<>();
    private TextView textViewRequestId, textViewDateSubmitted, textViewDirection,
            textViewFromBranch, textViewToBranch, textViewCreatedBy;
    private ProgressBar progressBarMain;
    private LinearLayout layoutRequestActions;
    private AppCompatButton buttonApprove, buttonReject;
    private MaterialButton buttonGoBack;

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
                boolean isTerminal = "COMPLETED".equals(requestStatus) ||
                        "REJECTED".equals(requestStatus) ||
                        "CANCELLED".equals(requestStatus);

                View bottomActionBar = findViewById(R.id.constraintLayout2);
                if (bottomActionBar != null) {
                    bottomActionBar.setVisibility(isTerminal ? View.GONE : View.VISIBLE);
                }

                if (requestedItemsAdapter != null) {
                    requestedItemsAdapter.setReadOnly(isTerminal);
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

                //initializeRecyclerViews();
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

        buttonApprove = findViewById(R.id.button_accept);
        buttonReject = findViewById(R.id.button_reject_cancel);
        buttonGoBack = findViewById(R.id.button_request_details_go_back);
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
        if (buttonGoBack != null) buttonGoBack.setOnClickListener(v -> finish());
        if (buttonApprove != null) buttonApprove.setOnClickListener(v -> handleApproveRequest());
        if (buttonReject != null) buttonReject.setOnClickListener(v -> handleRejectRequest());
    }

    private void handleApproveRequest() {
        int requestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        if (requestId == -1) return;

        List<ApproveRequestDTO> approvals = requestedItemsAdapter.getApprovalList();
        if (approvals.isEmpty()) {
            Toast.makeText(this, "No items to approve", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestRepository repository = new RequestRepository(this);
        repository.approveRequest(requestId, approvals).enqueue(new SafeCallback<ApprovalResponse>(this, progressBarMain) {
            @Override
            public void onSuccess(ApprovalResponse response) {
                Toast.makeText(RequestDetailsActivity.this, "Request approved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void handleRejectRequest() {
        int requestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        if (requestId == -1) return;

        RejectRequestDTO dto = new RejectRequestDTO();
        dto.setRemarks("Request rejected");

        RequestRepository repository = new RequestRepository(this);
        repository.rejectRequest(requestId, dto).enqueue(new SafeCallback<RejectResponse>(this, progressBarMain) {
            @Override
            public void onSuccess(RejectResponse response) {
                Toast.makeText(RequestDetailsActivity.this, "Request rejected", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}