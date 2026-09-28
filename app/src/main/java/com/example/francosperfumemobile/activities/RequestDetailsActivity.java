package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.RequestApprovalAdapter;
import com.example.francosperfumemobile.adapters.RequestedItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestDetailResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDetailsActivity extends AppCompatActivity {

    private final List<DisplayRequestItemDetailsDTO> requestedProductsList = new ArrayList<>();
    private final List<DisplayRequestApprovalDetailsDTO> requestTimelineList = new ArrayList<>();

    private TextView textViewRequestId, textViewDateSubmitted, textViewDirection,
            textViewFromBranch, textViewToBranch, textViewCreatedBy;
    private ProgressBar progressBarMain;
    private MaterialButton buttonGoBack;
    private AppCompatButton buttonAccept, buttonRejectCancel, buttonOpenMessage;

    private RecyclerView requestApprovalRecyclerView, requestedProductsRecyclerView;
    private RequestApprovalAdapter requestApprovalAdapter;
    private RequestedItemsAdapter requestedItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeRecyclerViews();
        initializeListeners();

        Intent intent = getIntent();
        int requestId = intent.getIntExtra("REQUEST_ID", -1);
        if (requestId == -1) {
            Toast.makeText(this, "Invalid request ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchRequestDetails(requestId);
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
        requestedProductsRecyclerView = findViewById(R.id.recycler_view_requested_products);
        requestApprovalRecyclerView = findViewById(R.id.recycler_view_request_timeline);

        requestedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestApprovalRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestedItemsAdapter = new RequestedItemsAdapter(requestedProductsList);
        requestApprovalAdapter = new RequestApprovalAdapter(requestTimelineList);

        requestedProductsRecyclerView.setAdapter(requestedItemsAdapter);
        requestApprovalRecyclerView.setAdapter(requestApprovalAdapter);
    }

    private void initializeListeners() {
        findViewById(R.id.button_request_details_go_back).setOnClickListener(v -> finish());
    }

    private void fetchRequestDetails(int requestId) {
        if (progressBarMain != null) {
            progressBarMain.setVisibility(View.VISIBLE);
        }

        RequestRepository repository = new RequestRepository(this);
        repository.getRequestDetails(requestId).enqueue(new Callback<RequestDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<RequestDetailResponse> call, @NonNull Response<RequestDetailResponse> response) {
                if (isFinishing() || isDestroyed()) return;

                if (progressBarMain != null) {
                    progressBarMain.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null) {
                    RequestDetailResponse detailResponse = response.body();

                    textViewRequestId.setText(detailResponse.getData().getRequestDisplayId());
                    textViewDateSubmitted.setText(String.format("Date submitted: %s", detailResponse.getData().getRequestDateSubmitted()));
                    textViewFromBranch.setText(detailResponse.getData().getRequestedFrom());
                    textViewToBranch.setText(detailResponse.getData().getDeliveredTo());
                    //TODO: Employee name should be fetched from backend
                    textViewCreatedBy.setText(detailResponse.getData().getEmployeeDisplayId());

                    requestedProductsList.clear();
                    requestedProductsList.addAll(detailResponse.getData().getItems());
                    requestedItemsAdapter.notifyDataSetChanged();

                    requestTimelineList.clear();
                    requestTimelineList.addAll(detailResponse.getData().getApprovals());
                    requestApprovalAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(RequestDetailsActivity.this, "Failed to fetch request details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestDetailResponse> call, @NonNull Throwable t) {
                if (isFinishing() || isDestroyed()) return;

                if (progressBarMain != null) {
                    progressBarMain.setVisibility(View.GONE);
                }
                Toast.makeText(RequestDetailsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}