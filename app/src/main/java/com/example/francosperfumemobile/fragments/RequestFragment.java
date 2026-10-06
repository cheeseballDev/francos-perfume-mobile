package com.example.francosperfumemobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.RequestDetailsActivity;
import com.example.francosperfumemobile.adapters.RequestAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestFilterDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestProductFilterDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestFiltersResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestListResponse;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.example.francosperfumemobile.helpers.PaginationHelper;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestFragment extends Fragment {

    private final List<DisplayRequestDTO> requestList = new ArrayList<>();
    private final RequestFilterDTO currentFilter = new RequestFilterDTO();
    private RequestAdapter requestAdapter;

    private Spinner dropdownDirection, dropdownStatus, dropdownStage;
    private MaterialButton buttonAll, buttonInbound, buttonOutbound;
    private TextView textViewPagination;
    private EditText editTextSearch;
    private ImageButton buttonNextPage, buttonLastPage;
    private ProgressBar progressBarRequest, progressBarPagination;
    private int totalRequestCount = 0;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance() {
        return new RequestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeUI(view);
        initializeRecyclerView(view);
        initializeDropdowns();
        fetchRequests(currentFilter, true);
    }

    private void fetchRequests(RequestFilterDTO filter, boolean isInitialFetch) {
        PaginationHelper.setLoadingState(isInitialFetch, true, progressBarRequest, progressBarPagination, buttonNextPage);

        RequestRepository repository = new RequestRepository(requireContext());
        repository.displayRequests(filter).enqueue(new SafeCallback<RequestListResponse>(this, progressBarRequest) {
            @Override
            public void onSuccess(RequestListResponse response) {
                List<DisplayRequestDTO> items = response.getData();
                totalRequestCount = response.getTotalRequests();

                if (isInitialFetch) {
                    requestAdapter.updateData(items != null ? items : new ArrayList<>());
                } else if (items != null && !items.isEmpty()) {
                    requestAdapter.addData(items);
                }

                PaginationHelper.updatePagination(
                        requestAdapter.getItemCount(),
                        totalRequestCount,
                        textViewPagination,
                        buttonNextPage
                );
            }
        });
    }

    private void initializeDropdowns() {
        RequestRepository repository = new RequestRepository(requireContext());
        repository.getRequestFilters().enqueue(new SafeCallback<RequestFiltersResponse>(this) {
            @Override
            public void onSuccess(RequestFiltersResponse filterResponse) {
                List<String> productNames = new ArrayList<>();
                if (filterResponse.getProducts() != null) {
                    for (RequestProductFilterDTO product : filterResponse.getProducts()) {
                        if (product != null && product.getProductName() != null) {
                            productNames.add(product.getProductName());
                        }
                    }
                }

                FilterManager.setupSpinner(requireContext(), dropdownDirection, productNames, "All products");
                FilterManager.setupSpinner(requireContext(), dropdownStatus, filterResponse.getRequestStatus(), "All status");
                FilterManager.setupSpinner(requireContext(), dropdownStage, filterResponse.getStages(), "All stages");

                initializeSpinnerListeners();
            }
        });
    }

    private void initializeSpinnerListeners() {
        dropdownStage.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedStage = (position == 0) ? null : value;

                if (!Objects.equals(currentFilter.getStage(), selectedStage)) {
                    currentFilter.setStage(selectedStage);
                    currentFilter.setPageCount(1);
                    fetchRequests(currentFilter, true);
                }
            }
        });

        dropdownStatus.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedStatus = (position == 0) ? null : value;

                if (!Objects.equals(currentFilter.getRequestStatus(), selectedStatus)) {
                    currentFilter.setRequestStatus(selectedStatus);
                    currentFilter.setPageCount(1);
                    fetchRequests(currentFilter, true);
                }
            }
        });

        dropdownDirection.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedDirection = (position == 0) ? null : value;

                if (!Objects.equals(currentFilter.getDirection(), selectedDirection)) {
                    currentFilter.setDirection(selectedDirection);
                    currentFilter.setPageCount(1);
                    fetchRequests(currentFilter, true);
                }
            }
        });
    }

    private void initializeRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_request);
        requestAdapter = new RequestAdapter(requestList, request -> {
            startActivity(RequestDetailsActivity.newIntent(requireContext(), request.getRequestId()));
        });
        RecyclerViewHelper.setupVertical(requireContext(), recyclerView, requestAdapter);
    }

    private void initializeUI(View view) {
        dropdownDirection = view.findViewById(R.id.dropdown_direction);
        dropdownStage = view.findViewById(R.id.dropdown_stage);
        dropdownStatus = view.findViewById(R.id.dropdown_status);

        buttonAll = view.findViewById(R.id.button_request_all);
        buttonInbound = view.findViewById(R.id.button_request_inbound);
        buttonOutbound = view.findViewById(R.id.button_request_outbound);

        buttonNextPage = view.findViewById(R.id.button_inventory_next_page);
        buttonLastPage = view.findViewById(R.id.button_inventory_last_page);

        editTextSearch = view.findViewById(R.id.edit_text_search);

        progressBarRequest = view.findViewById(R.id.progress_bar_request);
        progressBarPagination = view.findViewById(R.id.progress_bar_request_pagination);

        textViewPagination = view.findViewById(R.id.text_view_request_pagination);
    }
}