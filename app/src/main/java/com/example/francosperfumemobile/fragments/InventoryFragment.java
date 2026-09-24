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
import com.example.francosperfumemobile.activities.InventoryBatchListActivity;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.InventorySearchFilterDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryFilterResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.helpers.FilterManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryFragment extends Fragment {

    private final List<DisplayInventoryDTO> inventoryList = new ArrayList<>();
    private Spinner dropdownPerfumeType, dropdownGenderType, dropdownBranch;
    private EditText editTextSearch;
    private TextView textViewPagination;
    private ImageButton buttonNextPage, buttonLastPage;
    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private final InventorySearchFilterDTO currentFilter = new InventorySearchFilterDTO();
    private ProgressBar progressBarMain, progressBarPagination;
    private int totalItemCount = 0;
    private boolean isLoading = false;


    public InventoryFragment() {
        // Required empty public constructoraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    }

    // can use this to create local variables or get variables from other activities/fragments to this specific one
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }


    //i know it looks like a complete fucking mess, but ill move them to their own service soon if possible
    //i just need to get them to work
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeUI(view);
        initializeRecyclerView(view);
        intializeListeners();
        initializeDropdowns(dropdownPerfumeType, dropdownGenderType, dropdownBranch);
        fetchInventory(currentFilter, true);
        // TODO: Add the pagination here using the new FilterManager.
    }

    private void fetchInventory(InventorySearchFilterDTO filter, boolean isInitialFetch) {
        setLoadingState(isInitialFetch, true);
        progressBarMain.setVisibility(View.VISIBLE);

        InventoryRepository repository = new InventoryRepository(requireContext());
        repository.displayInventory(filter).enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                if (!isAdded() || getContext() == null) return;

                progressBarMain.setVisibility(View.GONE);

                setLoadingState(isInitialFetch, false);

                if (response.isSuccessful() && response.body() != null) {
                    List<DisplayInventoryDTO> items = response.body().getData();
                    totalItemCount = response.body().getTotalInventories();

                    if (isInitialFetch) {
                        inventoryAdapter.updateData(items != null ? items : new ArrayList<>());
                    } else if (items != null && !items.isEmpty()) {
                        inventoryAdapter.addData(items);
                    }

                    updateResultCounterAndButton();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch inventory", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryResponse> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    progressBarMain.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeUI(View view) {
        editTextSearch = view.findViewById(R.id.edit_text_search);
        dropdownPerfumeType = view.findViewById(R.id.dropdown_perfume_type);
        dropdownGenderType = view.findViewById(R.id.dropdown_gender_type);
        dropdownBranch = view.findViewById(R.id.dropdown_branch);

        progressBarMain = view.findViewById(R.id.progress_bar_main);
        progressBarPagination = view.findViewById(R.id.progress_bar_pagination);
    }

    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


         inventoryAdapter = new InventoryAdapter(inventoryList, selectedProduct -> {
            Intent intent = InventoryBatchListActivity.newIntent(requireContext(), selectedProduct.getProductId());
            startActivity(intent);
        });
        recyclerView.setAdapter(inventoryAdapter);
    }

    public void initializeDropdowns(Spinner typeSpinner, Spinner genderSpinner, Spinner branchSpinner) {
        InventoryRepository repository = new InventoryRepository(requireContext());
        repository.getInventoryFilters().enqueue(new Callback<InventoryFilterResponse>() {
            @Override
            public void onResponse(Call<InventoryFilterResponse> call, Response<InventoryFilterResponse> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    InventoryFilterResponse filterResponse = response.body();

                    FilterManager.setupSpinner(requireContext(), typeSpinner, filterResponse.getProductTypes(), "All Types");
                    FilterManager.setupSpinner(requireContext(), genderSpinner, filterResponse.getProductGenders(), "All Genders");
                    FilterManager.setupSpinner(requireContext(), branchSpinner, filterResponse.getBranches(), "All Branches");
                } else {
                    Toast.makeText(getContext(), "Failed to load filters", Toast.LENGTH_SHORT).show();
                }

                initializeSpinnerListeners();
            }

            @Override
            public void onFailure(Call<InventoryFilterResponse> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeSpinnerListeners() {
        dropdownPerfumeType.setOnItemSelectedListener(new FilterManager.SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position, String value) {
                String selectedType = (position == 0) ? null : value;

                if (!Objects.equals(currentFilter.getProductType(), selectedType)) {
                    currentFilter.setProductType(selectedType);
                    currentFilter.setPageCount(1); // Reset to page 1 on filter change
                    fetchInventory(currentFilter, true);
                }
            }
        });
    }

    private void intializeListeners() {
        // TODO: Add needed listeners
    }

    private void resetPaginationAndFetch() {
        currentFilter.setPageCount(1);
        fetchInventory(currentFilter, true);
    }

    private void updateResultCounterAndButton() {
        int loadedCount = inventoryAdapter.getItemCount();

        if (totalItemCount > 0) {
            String countText = String.format(Locale.getDefault(), "Showing %d of %d items", loadedCount, totalItemCount);
            textViewPagination.setText(countText);
            textViewPagination.setVisibility(View.VISIBLE);
        } else {
            textViewPagination.setText("No items found");
            textViewPagination.setVisibility(View.VISIBLE);
        }

        if (loadedCount >= totalItemCount || loadedCount == 0) {
            buttonNextPage.setVisibility(View.GONE);
        } else {
            buttonNextPage.setVisibility(View.VISIBLE);
        }
    }

    // Helper to manage UI progress indicators cleanly
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

    private void showLoading(boolean isPageChange) {
        if (isPageChange) {
            textViewPagination.setVisibility(View.GONE);
            progressBarPagination.setVisibility(View.VISIBLE);
            buttonNextPage.setEnabled(false);
            buttonLastPage.setEnabled(false);
        } else {
            progressBarMain.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void hideLoading() {
        progressBarMain.setVisibility(View.GONE);
        progressBarPagination.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
        textViewPagination.setVisibility(View.VISIBLE);
        buttonNextPage.setEnabled(true);
        buttonLastPage.setEnabled(true);
    }
}