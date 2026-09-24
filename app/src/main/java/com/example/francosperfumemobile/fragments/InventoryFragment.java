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
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryFragment extends Fragment {

    private final List<DisplayInventoryDTO> inventoryList = new ArrayList<>();
    private Spinner dropdownPerfumeType, dropdownGenderType, dropdownBranch;
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private final InventorySearchFilterDTO currentFilter = new InventorySearchFilterDTO();
    private ProgressBar progressBar;


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
        fetchInventory(currentFilter);
        // TODO: Add the pagination here using the new FilterManager.

    }
    private void fetchInventory(InventorySearchFilterDTO filter) {
        progressBar.setVisibility(View.VISIBLE);

        InventoryRepository repository = new InventoryRepository(requireContext());
        repository.displayInventory(filter).enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                if (!isAdded() || getContext() == null) return;

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<DisplayInventoryDTO> items = response.body().getData();
                    inventoryAdapter.updateData(items);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch inventory", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryResponse> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*


     */

    private void initializeUI(View view) {
        editTextSearch = view.findViewById(R.id.edit_text_search);
        dropdownPerfumeType = view.findViewById(R.id.dropdown_perfume_type);
        dropdownGenderType = view.findViewById(R.id.dropdown_gender_type);
        dropdownBranch = view.findViewById(R.id.dropdown_branch);
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
                    fetchInventory(currentFilter);
                }
            }
        });
    }

    private void intializeListeners() {
        // TODO: Add needed listeners
    }
}