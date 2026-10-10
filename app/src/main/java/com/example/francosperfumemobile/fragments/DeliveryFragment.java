package com.example.francosperfumemobile.fragments;

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

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.DeliveryDetailsActivity;
import com.example.francosperfumemobile.adapters.DeliveryAdapter;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryFilterDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.backend.repository.DeliveryRepository;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryFiltersResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryListResponse;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.example.francosperfumemobile.helpers.PaginationHelper;
import com.example.francosperfumemobile.helpers.RecyclerViewHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliveryFragment extends Fragment {

    private final List<DisplayDeliveryDTO> deliveryList = new ArrayList<>();
    private final DeliveryFilterDTO currentFilter = new DeliveryFilterDTO();
    private TextView textViewPagination;
    private Spinner dropdownFromBranch, dropdownToBranch, dropdownStatus;
    private ImageButton buttonNextPage, buttonLastPage;
    private MaterialButton buttonAll, buttonInbound, buttonOutbound;
    private DeliveryAdapter deliveryAdapter;
    private ProgressBar progressBarDelivery, progressBarPagination;
    private EditText editTextSearch;
    private int totalDeliveryCount = 0;

    public DeliveryFragment() {

    }

    public static DeliveryFragment newInstance(String param1, String param2) {
        DeliveryFragment fragment = new DeliveryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedBundleInstance) {
        // TODO: delivery

        initializeUI(view);
        initializeRecyclerView(view);
        initializeDropdowns();
        fetchDeliveries(currentFilter, true);
    }

    private void fetchDeliveries(DeliveryFilterDTO filter, boolean isInitialFetch) {
        PaginationHelper.setLoadingState(isInitialFetch, true, progressBarDelivery, progressBarPagination, buttonNextPage);

        DeliveryRepository repository = new DeliveryRepository(requireContext());
        repository.displayDeliveries(filter).enqueue(new SafeCallback<DeliveryListResponse>(this, progressBarDelivery) {
            @Override
            public void onSuccess(DeliveryListResponse response) {
                List<DisplayDeliveryDTO> items = response.getData();
                totalDeliveryCount = response.getTotalDeliveries();

                if (isInitialFetch) {
                    deliveryAdapter.updateData(items != null ? items : new ArrayList<>());
                } else if (items != null && !items.isEmpty()) {
                    deliveryAdapter.addData(items);
                }

                PaginationHelper.updatePagination(
                        deliveryAdapter.getItemCount(),
                        totalDeliveryCount,
                        textViewPagination,
                        buttonNextPage
                );
            }
        });
    }

    private void initializeDropdowns() {
        DeliveryRepository repository = new DeliveryRepository(requireContext());

        repository.getDeliveryFilters().enqueue(new SafeCallback<DeliveryFiltersResponse>(this) {
            @Override
            public void onSuccess(DeliveryFiltersResponse filterResponse) {
                FilterManager.setupSpinner(requireContext(), dropdownFromBranch, filterResponse.getFromBranches(), "All Branches");
                FilterManager.setupSpinner(requireContext(), dropdownToBranch, filterResponse.getToBranches(), "All To Branch");
                FilterManager.setupSpinner(requireContext(), dropdownStatus, filterResponse.getStatus(), "All From Branch");

                initializeSpinnerAndSearchListeners();
            }
        });
    }

    private void initializeSpinnerAndSearchListeners() {
        dropdownFromBranch.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedFromBranch = (position == 0) ? null : value;

                // todo: change into branchname instead of branchid
                if (!Objects.equals(currentFilter.getFromBranch(), selectedFromBranch)) {
                    currentFilter.setFromBranch(selectedFromBranch);
                    currentFilter.setPageCount(1);
                    fetchDeliveries(currentFilter, true);
                }
            }
        });

        dropdownStatus.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedStatus = (position == 0) ? null : value;

                if (!Objects.equals(currentFilter.getStatus(), selectedStatus)) {
                    currentFilter.setSearch(selectedStatus);
                    currentFilter.setPageCount(1);
                    fetchDeliveries(currentFilter, true);
                }
            }
        });

        dropdownToBranch.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedDirection = (position == 0) ? null : value;

                if (!Objects.equals(currentFilter.getToBranch(), selectedDirection)) {
                    currentFilter.setToBranch(selectedDirection);
                    currentFilter.setPageCount(1);
                    fetchDeliveries(currentFilter, true);
                }
            }
        });

        editTextSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                String query = s.toString().trim();
                currentFilter.setSearch(query.isEmpty() ? null : query);
                currentFilter.setPageCount(1);
                fetchDeliveries(currentFilter, true);
            }
        });
    }

    private void initializeUI(View view) {
        textViewPagination = view.findViewById(R.id.text_view_delivery_pagination);

        dropdownFromBranch = view.findViewById(R.id.dropdown_from_branch);
        dropdownToBranch = view.findViewById(R.id.dropdown_to_branch);
        dropdownStatus = view.findViewById(R.id.dropdown_status);

        buttonNextPage = view.findViewById(R.id.button_delivery_next_page);
        buttonLastPage = view.findViewById(R.id.button_delivery_last_page);

        buttonAll = view.findViewById(R.id.button_delivery_all);
        buttonInbound = view.findViewById(R.id.button_delivery_inbound);
        buttonOutbound = view.findViewById(R.id.button_delivery_outbound);

        progressBarDelivery = view.findViewById(R.id.progress_bar_delivery);
        progressBarPagination = view.findViewById(R.id.progress_bar_delivery_pagination);

        editTextSearch = view.findViewById(R.id.edit_text_search);

        initializeListeners();
    }

    private void initializeListeners() {
        // todo: add buttons & functionalities here
    }

    private void initializeRecyclerView(View view) {
        RecyclerView recyclerViewDelivery = view.findViewById(R.id.recycler_view_delivery);
        recyclerViewDelivery.setLayoutManager(new LinearLayoutManager(requireContext()));

        deliveryAdapter = new DeliveryAdapter(deliveryList, selectedDelivery -> {
            startActivity(DeliveryDetailsActivity.newIntent(requireContext(), selectedDelivery.getDeliveryId()));
        });
        RecyclerViewHelper.setupVertical(requireContext(), recyclerViewDelivery, deliveryAdapter);
    }
}