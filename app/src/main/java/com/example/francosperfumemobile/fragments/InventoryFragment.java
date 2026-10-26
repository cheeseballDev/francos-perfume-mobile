package com.example.francosperfumemobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.InventoryBatchListActivity;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.InventorySearchFilterDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryFilterResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.example.francosperfumemobile.helpers.ListScreenState;
import com.example.francosperfumemobile.helpers.PaginationHelper;
import com.example.francosperfumemobile.helpers.SafeCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Inventory list with search, filters and page-by-page navigation.
 * Previous/Next replace the list (no more duplicated rows), taps are ignored while a page loads,
 * and search waits until typing stops.
 */
public class InventoryFragment extends Fragment {

    private final InventorySearchFilterDTO currentFilter = new InventorySearchFilterDTO();
    private InventoryAdapter inventoryAdapter;
    private PaginationHelper pagination;
    private ListScreenState listState;
    private DebouncedTextWatcher searchWatcher;
    private AutoCompleteTextView dropdownPerfumeType, dropdownGenderType, dropdownBranch;
    private Call<InventoryResponse> currentCall;

    public InventoryFragment() {
        // Required empty public constructor
    }

    public static InventoryFragment newInstance() {
        return new InventoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        inventoryAdapter = new InventoryAdapter(new ArrayList<>(), this::openInventoryBatchList);
        recyclerView.setAdapter(inventoryAdapter);

        SwipeRefreshLayout swipe = view.findViewById(R.id.swipe_refresh_inventory);
        listState = new ListScreenState(swipe, view.findViewById(R.id.progress_bar_inventory),
                view.findViewById(R.id.layout_empty_inventory), recyclerView);
        listState.setEmptyIcon(R.drawable.ic_boxes);
        listState.setOnRefresh(() -> fetchInventory(currentFilter.getPageCount()));

        View pager = view.findViewById(R.id.layout_pagination);
        pagination = new PaginationHelper(pager.findViewById(R.id.button_page_previous),
                pager.findViewById(R.id.button_page_next),
                pager.findViewById(R.id.text_view_page_label),
                this::fetchInventory);

        EditText editTextSearch = view.findViewById(R.id.edit_text_search);
        searchWatcher = new DebouncedTextWatcher(query -> {
            currentFilter.setSearch(query.isEmpty() ? null : query);
            fetchInventory(1);
        });
        searchWatcher.setLastQuery("");
        editTextSearch.addTextChangedListener(searchWatcher);

        dropdownPerfumeType = dropdown(view, R.id.dropdown_perfume_type);
        dropdownGenderType = dropdown(view, R.id.dropdown_gender_type);
        dropdownBranch = dropdown(view, R.id.dropdown_branch);
        dropdownPerfumeType.setText(getString(R.string.filter_all_types), false);
        dropdownGenderType.setText(getString(R.string.filter_all_genders), false);
        dropdownBranch.setText(getString(R.string.filter_all_branches), false);

        initializeDropdowns();
        fetchInventory(1);
    }

    @Override
    public void onDestroyView() {
        if (searchWatcher != null) searchWatcher.cancel();
        if (currentCall != null) currentCall.cancel();
        super.onDestroyView();
    }

    static AutoCompleteTextView dropdown(View root, int includeId) {
        return root.findViewById(includeId).findViewById(R.id.dropdown_filter);
    }

    private void fetchInventory(int page) {
        if (currentCall != null) currentCall.cancel(); // only the latest search/page counts
        currentFilter.setPageCount(page);
        pagination.setLoading(true);
        listState.showLoading(inventoryAdapter.getItemCount() > 0);

        currentCall = new InventoryRepository(requireContext()).displayInventory(currentFilter);
        currentCall.enqueue(new SafeCallback<InventoryResponse>(this) {
            @Override
            public void onSuccess(@Nullable InventoryResponse response) {
                List<DisplayInventoryDTO> items = response != null ? response.getData() : null;
                inventoryAdapter.setItems(items);
                int total = response != null ? response.getTotalInventories() : 0;
                int pages = response != null ? response.getTotalInventoriesPages() : 0;
                pagination.onPageLoaded(page, pages, total, currentFilter.getPageSize());
                listState.showResult(inventoryAdapter.getItemCount(), hasFilters());
            }

            @Override
            public void onError(@NonNull String message, int code) {
                pagination.setLoading(false);
                if (code == 401) {
                    listState.stopLoading();
                    return;
                }
                if (!listState.showError(message, inventoryAdapter.getItemCount() > 0, () -> fetchInventory(page))) {
                    super.onError(message, code);
                }
            }
        });
    }

    private boolean hasFilters() {
        return currentFilter.getSearch() != null || currentFilter.getProductType() != null
                || currentFilter.getProductGender() != null || currentFilter.getBranch() != null;
    }

    private void openInventoryBatchList(DisplayInventoryDTO product) {
        // Pass the row's branch, so an Owner sees the batches of the branch they tapped
        Intent intent = InventoryBatchListActivity.newIntent(requireContext(), product);
        startActivity(intent);
    }

    private void initializeDropdowns() {
        new InventoryRepository(requireContext()).getInventoryFilters().enqueue(new SafeCallback<InventoryFilterResponse>(this) {
            @Override
            public void onSuccess(@Nullable InventoryFilterResponse filters) {
                if (filters == null) return;
                FilterManager.setupDropdown(requireContext(), dropdownPerfumeType, filters.getProductTypes(),
                        getString(R.string.filter_all_types), value -> {
                            currentFilter.setProductType(value);
                            fetchInventory(1);
                        });
                FilterManager.setupDropdown(requireContext(), dropdownGenderType, filters.getProductGenders(),
                        getString(R.string.filter_all_genders), value -> {
                            currentFilter.setProductGender(value);
                            fetchInventory(1);
                        });
                FilterManager.setupDropdown(requireContext(), dropdownBranch, filters.getBranches(),
                        getString(R.string.filter_all_branches), value -> {
                            currentFilter.setBranch(value);
                            fetchInventory(1);
                        });
            }

            @Override
            public void onError(@NonNull String message, int code) {
                // The list still works without filters; don't show a second error
            }
        });
    }
}
