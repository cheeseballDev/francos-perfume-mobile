package com.example.francosperfumemobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.RequestDetailsActivity;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.adapters.RequestAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.InventorySearchFilterDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestFilterDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestProductFilterDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryFilterResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestFiltersResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestListResponse;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment {

    private final List<DisplayRequestDTO> requestList = new ArrayList<>();
    private RequestAdapter requestAdapter;
    private final RequestFilterDTO currentFilter = new RequestFilterDTO();
    private Spinner dropdownDirection, dropdownStatus, dropdownStage;
    private MaterialButton buttonAll, buttonInbound, buttonOutbound;
    private TextView textViewPagination;
    private EditText editTextSearch;
    private ImageButton buttonNextPage, buttonLastPage;
    private ProgressBar progressBarMain, progressBarPagination;
    private boolean isLoading;
    private int totalRequestCount = 0;


    public RequestFragment() {
        // constructor for whatever
    }

    public static RequestFragment newInstance() {
        RequestFragment fragment = new RequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RequestFilterDTO filterDTO = new RequestFilterDTO();
        initializeUI(view);
        initializeRecyclerView(view);
        initializeListeners();
        initializeDropdowns();
        initializeSpinnerListeners();
        fetchRequests(filterDTO, true);
    }

    private void fetchRequests(RequestFilterDTO filter, boolean isInitialFetch) {
        setLoadingState(isInitialFetch, true);
        progressBarMain.setVisibility(View.VISIBLE);

        RequestRepository repository = new RequestRepository(requireContext());
        //TODO: Fix this.
        repository.displayRequests(filter).enqueue(new Callback<RequestListResponse>() {
            @Override
            public void onResponse(Call<RequestListResponse> call, Response<RequestListResponse> response) {
                if (!isAdded() || getContext() == null) return;

                progressBarMain.setVisibility(View.GONE);

                setLoadingState(isInitialFetch, false);

                if (response.isSuccessful() && response.body() != null) {
                    List<DisplayRequestDTO> items = response.body().getData();
                    totalRequestCount = response.body().getTotalRequests();

                    if (isInitialFetch) {
                        requestAdapter.updateData(items != null ? items : new ArrayList<>());
                    } else if (items != null && !items.isEmpty()) {
                        requestAdapter.addData(items);
                    }

                    updateResultCounterAndButton();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch requests", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestListResponse> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    progressBarMain.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openRequestDetails(int requestId) {
        Intent intent = new Intent(requireContext(), RequestDetailsActivity.class);
        intent.putExtra("REQUEST_ID", requestId);

        startActivity(intent);
    }


    private void setLoadingState(boolean isInitialFetch, boolean isLoading) {
        this.isLoading = isLoading && !isInitialFetch;

        if (isInitialFetch) {
            if (progressBarMain != null) {
                progressBarMain.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            if (isLoading) {
                progressBarMain.setVisibility(View.GONE);
            }
        } else {
            progressBarPagination.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            buttonNextPage.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }

    private void updateResultCounterAndButton() {
        int loadedCount = requestAdapter.getItemCount();

        if (totalRequestCount > 0) {
            String countText = String.format(Locale.getDefault(), "Showing %d of %d items", loadedCount, totalRequestCount);
            textViewPagination.setText(countText);
            textViewPagination.setVisibility(View.VISIBLE);
        } else {
            textViewPagination.setText("No items found");
            textViewPagination.setVisibility(View.VISIBLE);
        }

        if (loadedCount >= totalRequestCount || loadedCount == 0) {
            buttonNextPage.setVisibility(View.GONE);
        } else {
            buttonNextPage.setVisibility(View.VISIBLE);
        }
    }

    public void initializeDropdowns() {
        RequestRepository repository = new RequestRepository(requireContext());
        repository.getRequestFilters().enqueue(new Callback<RequestFiltersResponse>() {
            @Override
            public void onResponse(Call<RequestFiltersResponse> call, Response<RequestFiltersResponse> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    RequestFiltersResponse filterResponse = response.body();
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
                } else {
                    Toast.makeText(getContext(), "Failed to load filters", Toast.LENGTH_SHORT).show();
                }

                initializeSpinnerListeners();
            }

            @Override
            public void onFailure(Call<RequestFiltersResponse> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeSpinnerListeners() {
        dropdownStage.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedStage = (position == 0) ? null : value;

                // TODO: Add stage filtering in RequestFilterDTO
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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        requestAdapter = new RequestAdapter(requestList, request -> {
            openRequestDetails(request.getRequestId());
        });
        recyclerView.setAdapter(requestAdapter);
    }

    private void initializeUI(View view) {
        dropdownDirection = view.findViewById(R.id.dropdown_direction);
        dropdownStage = view.findViewById(R.id.dropdown_stage);
        dropdownStatus = view.findViewById(R.id.dropdown_status);

        buttonAll = view.findViewById(R.id.button_request_all);
        buttonInbound = view.findViewById(R.id.button_request_inbound);
        buttonOutbound = view.findViewById(R.id.button_request_outbound);

        buttonNextPage = view.findViewById(R.id.button_next_page);
        buttonLastPage = view.findViewById(R.id.button_last_page);

        editTextSearch = view.findViewById(R.id.edit_text_search);

        progressBarMain = view.findViewById(R.id.progress_bar_main);
        progressBarPagination = view.findViewById(R.id.progress_bar_pagination);

        textViewPagination = view.findViewById(R.id.text_view_request_pagination);
    }

    private void initializeListeners() {

    }
}