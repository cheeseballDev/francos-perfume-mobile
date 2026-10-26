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
import com.example.francosperfumemobile.activities.CreateRequestActivity;
import com.example.francosperfumemobile.activities.RequestDetailsActivity;
import com.example.francosperfumemobile.adapters.RequestAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestFilterDTO;
import com.example.francosperfumemobile.backend.repository.RequestRepository;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestFiltersResponse;
import com.example.francosperfumemobile.backend.responses.requestresponses.RequestListResponse;
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
 * Transfer requests: search, All/Inbound/Outbound, status and stage filters, paging.
 * The list reloads when you come back from a request you approved, rejected or cancelled.
 */
public class RequestFragment extends Fragment {

    private final RequestFilterDTO currentFilter = new RequestFilterDTO();
    private RequestAdapter requestAdapter;
    private PaginationHelper pagination;
    private ListScreenState listState;
    private DebouncedTextWatcher searchWatcher;
    private AutoCompleteTextView dropdownStatus, dropdownStage;
    private Call<RequestListResponse> currentCall;

    // Details screen returns RESULT_OK after an action, so the list shows the new status
    private final ActivityResultLauncher<Intent> detailsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && isAdded()) {
                    fetchRequests(currentFilter.getPageCount());
                }
            });

    private final ActivityResultLauncher<Intent> createLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && isAdded()) fetchRequests(1);
            });

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance() {
        return new RequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_request);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        requestAdapter = new RequestAdapter(new ArrayList<>(), request ->
                detailsLauncher.launch(RequestDetailsActivity.newIntent(requireContext(), request.getRequestId())));
        recyclerView.setAdapter(requestAdapter);

        SwipeRefreshLayout swipe = view.findViewById(R.id.swipe_refresh_request);
        listState = new ListScreenState(swipe, view.findViewById(R.id.progress_bar_request),
                view.findViewById(R.id.layout_empty_request), recyclerView);
        listState.setEmptyIcon(R.drawable.ic_file_text);
        listState.setOnRefresh(() -> fetchRequests(currentFilter.getPageCount()));

        // Fixed: these used the inventory IDs before, so paging never worked here
        View pager = view.findViewById(R.id.layout_pagination);
        pagination = new PaginationHelper(pager.findViewById(R.id.button_page_previous),
                pager.findViewById(R.id.button_page_next),
                pager.findViewById(R.id.text_view_page_label),
                this::fetchRequests);

        EditText editTextSearch = view.findViewById(R.id.edit_text_search);
        searchWatcher = new DebouncedTextWatcher(query -> {
            currentFilter.setSearch(query.isEmpty() ? null : query);
            fetchRequests(1);
        });
        searchWatcher.setLastQuery("");
        editTextSearch.addTextChangedListener(searchWatcher);

        ChipGroup directionGroup = view.findViewById(R.id.chip_group_request_direction);
        directionGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String direction = null;
            if (checkedIds.contains(R.id.button_request_inbound)) direction = "INBOUND";
            else if (checkedIds.contains(R.id.button_request_outbound)) direction = "OUTBOUND";
            currentFilter.setDirection(direction);
            fetchRequests(1);
        });

        view.findViewById(R.id.fab_new_request).setOnClickListener(v ->
                createLauncher.launch(new Intent(requireContext(), CreateRequestActivity.class)));

        dropdownStatus = InventoryFragment.dropdown(view, R.id.dropdown_status);
        dropdownStage = InventoryFragment.dropdown(view, R.id.dropdown_stage);
        dropdownStatus.setText(getString(R.string.filter_all_statuses), false);
        dropdownStage.setText(getString(R.string.filter_all_stages), false);

        initializeDropdowns();
        fetchRequests(1);
    }

    @Override
    public void onDestroyView() {
        if (searchWatcher != null) searchWatcher.cancel();
        if (currentCall != null) currentCall.cancel();
        super.onDestroyView();
    }

    private void fetchRequests(int page) {
        if (currentCall != null) currentCall.cancel();
        currentFilter.setPageCount(page);
        pagination.setLoading(true);
        listState.showLoading(requestAdapter.getItemCount() > 0);

        currentCall = new RequestRepository(requireContext()).displayRequests(currentFilter);
        currentCall.enqueue(new SafeCallback<RequestListResponse>(this) {
            @Override
            public void onSuccess(@Nullable RequestListResponse response) {
                List<DisplayRequestDTO> items = response != null ? response.getData() : null;
                requestAdapter.setItems(items);
                int total = response != null ? response.getTotalRequests() : 0;
                int pages = response != null ? response.getTotalRequestPages() : 0;
                pagination.onPageLoaded(page, pages, total, currentFilter.getPageSize());
                listState.showResult(requestAdapter.getItemCount(), hasFilters());
            }

            @Override
            public void onError(@NonNull String message, int code) {
                pagination.setLoading(false);
                if (code == 401) {
                    listState.stopLoading();
                    return;
                }
                if (!listState.showError(message, requestAdapter.getItemCount() > 0, () -> fetchRequests(page))) {
                    super.onError(message, code);
                }
            }
        });
    }

    private boolean hasFilters() {
        return currentFilter.getSearch() != null || currentFilter.getRequestStatus() != null
                || currentFilter.getStage() != null || currentFilter.getDirection() != null;
    }

    private void initializeDropdowns() {
        new RequestRepository(requireContext()).getRequestFilters().enqueue(new SafeCallback<RequestFiltersResponse>(this) {
            @Override
            public void onSuccess(@Nullable RequestFiltersResponse filters) {
                if (filters == null) return;
                FilterManager.setupDropdown(requireContext(), dropdownStatus, filters.getRequestStatus(),
                        getString(R.string.filter_all_statuses), value -> {
                            currentFilter.setRequestStatus(value);
                            fetchRequests(1);
                        });
                FilterManager.setupDropdown(requireContext(), dropdownStage, filters.getStages(),
                        getString(R.string.filter_all_stages), value -> {
                            currentFilter.setStage(value);
                            fetchRequests(1);
                        });
            }

            @Override
            public void onError(@NonNull String message, int code) {
                // Filters are optional
            }
        });
    }
}
