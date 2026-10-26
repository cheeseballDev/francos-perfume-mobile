package com.example.francosperfumemobile.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.DeliveryDetailsActivity;
import com.example.francosperfumemobile.adapters.DeliveryAdapter;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryFilterDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.backend.repository.DeliveryRepository;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryFiltersResponse;
import com.example.francosperfumemobile.backend.responses.deliveryresponses.DeliveryListResponse;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.example.francosperfumemobile.helpers.ListScreenState;
import com.example.francosperfumemobile.helpers.PaginationHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Deliveries: search, All/Inbound/Outbound, status / from / to filters, paging.
 * Fixed: the status filter used to overwrite the search text, and Inbound/Outbound were never sent.
 */
public class DeliveryFragment extends Fragment {

    private final DeliveryFilterDTO currentFilter = new DeliveryFilterDTO();
    private DeliveryAdapter deliveryAdapter;
    private PaginationHelper pagination;
    private ListScreenState listState;
    private DebouncedTextWatcher searchWatcher;
    private AutoCompleteTextView dropdownFromBranch, dropdownToBranch, dropdownStatus;
    private Call<DeliveryListResponse> currentCall;

    private final ActivityResultLauncher<Intent> detailsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && isAdded()) {
                    fetchDeliveries(currentFilter.getPageCount());
                }
            });

    public DeliveryFragment() {
    }

    public static DeliveryFragment newInstance() {
        return new DeliveryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_delivery);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        deliveryAdapter = new DeliveryAdapter(new ArrayList<>(), delivery ->
                detailsLauncher.launch(DeliveryDetailsActivity.newIntent(requireContext(), delivery.getDeliveryId())));
        recyclerView.setAdapter(deliveryAdapter);

        SwipeRefreshLayout swipe = view.findViewById(R.id.swipe_refresh_delivery);
        listState = new ListScreenState(swipe, view.findViewById(R.id.progress_bar_delivery),
                view.findViewById(R.id.layout_empty_delivery), recyclerView);
        listState.setEmptyIcon(R.drawable.ic_truck);
        listState.setOnRefresh(() -> fetchDeliveries(currentFilter.getPageCount()));

        View pager = view.findViewById(R.id.layout_pagination);
        pagination = new PaginationHelper(pager.findViewById(R.id.button_page_previous),
                pager.findViewById(R.id.button_page_next),
                pager.findViewById(R.id.text_view_page_label),
                this::fetchDeliveries);

        EditText editTextSearch = view.findViewById(R.id.edit_text_search);
        searchWatcher = new DebouncedTextWatcher(query -> {
            currentFilter.setSearch(query.isEmpty() ? null : query);
            fetchDeliveries(1);
        });
        searchWatcher.setLastQuery("");
        editTextSearch.addTextChangedListener(searchWatcher);

        ChipGroup directionGroup = view.findViewById(R.id.chip_group_delivery_direction);
        directionGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String direction = null;
            if (checkedIds.contains(R.id.button_delivery_inbound)) direction = "INBOUND";
            else if (checkedIds.contains(R.id.button_delivery_outbound)) direction = "OUTBOUND";
            currentFilter.setDirection(direction);
            fetchDeliveries(1);
        });

        dropdownStatus = InventoryFragment.dropdown(view, R.id.dropdown_status);
        dropdownFromBranch = InventoryFragment.dropdown(view, R.id.dropdown_from_branch);
        dropdownToBranch = InventoryFragment.dropdown(view, R.id.dropdown_to_branch);
        dropdownStatus.setText(getString(R.string.filter_all_statuses), false);
        dropdownFromBranch.setText(getString(R.string.filter_all_from_branches), false);
        dropdownToBranch.setText(getString(R.string.filter_all_to_branches), false);

        initializeDropdowns();
        fetchDeliveries(1);
    }

    @Override
    public void onDestroyView() {
        if (searchWatcher != null) searchWatcher.cancel();
        if (currentCall != null) currentCall.cancel();
        super.onDestroyView();
    }

    private void fetchDeliveries(int page) {
        if (currentCall != null) currentCall.cancel();
        currentFilter.setPageCount(page);
        pagination.setLoading(true);
        listState.showLoading(deliveryAdapter.getItemCount() > 0);

        currentCall = new DeliveryRepository(requireContext()).displayDeliveries(currentFilter);
        currentCall.enqueue(new SafeCallback<DeliveryListResponse>(this) {
            @Override
            public void onSuccess(@Nullable DeliveryListResponse response) {
                List<DisplayDeliveryDTO> items = response != null ? response.getData() : null;
                deliveryAdapter.setItems(items);
                int total = response != null ? response.getTotalDeliveries() : 0;
                int pages = response != null ? response.getTotalDeliveryPages() : 0;
                pagination.onPageLoaded(page, pages, total, currentFilter.getPageSize());
                listState.showResult(deliveryAdapter.getItemCount(), hasFilters());
            }

            @Override
            public void onError(@NonNull String message, int code) {
                pagination.setLoading(false);
                if (code == 401) {
                    listState.stopLoading();
                    return;
                }
                if (!listState.showError(message, deliveryAdapter.getItemCount() > 0, () -> fetchDeliveries(page))) {
                    super.onError(message, code);
                }
            }
        });
    }

    private boolean hasFilters() {
        return currentFilter.getSearch() != null || currentFilter.getStatus() != null || currentFilter.getDirection() != null
                || currentFilter.getFromBranch() != null || currentFilter.getToBranch() != null;
    }

    private void initializeDropdowns() {
        new DeliveryRepository(requireContext()).getDeliveryFilters().enqueue(new SafeCallback<DeliveryFiltersResponse>(this) {
            @Override
            public void onSuccess(@Nullable DeliveryFiltersResponse filters) {
                if (filters == null) return;
                FilterManager.setupDropdown(requireContext(), dropdownStatus, filters.getStatus(),
                        getString(R.string.filter_all_statuses), value -> {
                            currentFilter.setStatus(value); // was setSearch(...) before
                            fetchDeliveries(1);
                        });
                FilterManager.setupDropdown(requireContext(), dropdownFromBranch, filters.getFromBranches(),
                        getString(R.string.filter_all_from_branches), value -> {
                            currentFilter.setFromBranch(value);
                            fetchDeliveries(1);
                        });
                FilterManager.setupDropdown(requireContext(), dropdownToBranch, filters.getToBranches(),
                        getString(R.string.filter_all_to_branches), value -> {
                            currentFilter.setToBranch(value);
                            fetchDeliveries(1);
                        });
            }

            @Override
            public void onError(@NonNull String message, int code) {
                // Filters are optional
            }
        });
    }
}
