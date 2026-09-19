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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.BranchDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.InventorySearchFilterDTO;
import com.example.francosperfumemobile.backend.repository.InventoryRepository;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryFilterResponse;
import com.example.francosperfumemobile.backend.responses.inventoryresponses.InventoryResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryFragment extends Fragment {

    private final List<DisplayInventoryDTO> inventoryList = new ArrayList<>();
    private Spinner dropdownPerfumeType, dropdownGenderType, dropdownBranch;
    private EditText textfieldSearch;


    public InventoryFragment() {
        // Required empty public constructoraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    }

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
        textfieldSearch = view.findViewById(R.id.textfield_search);
        dropdownPerfumeType = view.findViewById(R.id.dropdown_perfume_type);
        dropdownGenderType = view.findViewById(R.id.dropdown_gender_type);
        dropdownBranch = view.findViewById(R.id.dropdown_branch);
        loadDropdowns(dropdownPerfumeType, dropdownGenderType, dropdownBranch);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        InventoryAdapter adapter = new InventoryAdapter(inventoryList);
        recyclerView.setAdapter(adapter);

        InventoryRepository inventoryRepository = new InventoryRepository(requireContext());
        InventorySearchFilterDTO filter = new InventorySearchFilterDTO();


        filter.setSearch(null);
        filter.setProductType(null);
        filter.setProductGender(null);
        filter.setBranch(null);
        filter.setFromDate(null);
        filter.setToDate(null);
        filter.setPageCount(1);
        filter.setPageSize(20);

        Call<InventoryResponse> call = inventoryRepository.displayInventory(filter);
        call.enqueue(new Callback<InventoryResponse>() {
            @Override
            public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InventoryResponse inventoryResponse = response.body();
                    List<DisplayInventoryDTO> data = inventoryResponse.getData();
                    if (data != null && !data.isEmpty()) {
                        inventoryList.clear();
                        inventoryList.addAll(data);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "No inventory data found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load inventory" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadDropdowns(Spinner type, Spinner gender, Spinner branch) {
        InventoryRepository repository = new InventoryRepository(requireContext());
        Call<InventoryFilterResponse> call = repository.getInventoryFilters();
        call.enqueue(new Callback<InventoryFilterResponse>() {
            @Override
            public void onResponse(Call<InventoryFilterResponse> call, Response<InventoryFilterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InventoryFilterResponse filterResponse = response.body();

                    List<String> productTypes = filterResponse.getProductTypes();
                    if (productTypes != null) {
                        ArrayAdapter<String> productTypeAdapter =
                                new ArrayAdapter<>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        productTypes
                                );

                        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        type.setAdapter(productTypeAdapter);
                    }

                    List<String> productGenders = filterResponse.getProductGenders();
                    if (productGenders != null) {
                        ArrayAdapter<String> genderAdapter =
                                new ArrayAdapter<>(requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        productGenders
                                );

                        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gender.setAdapter(genderAdapter);
                    }

                    List<BranchDTO> branches = filterResponse.getBranches();
                    if(branches != null){
                        List<Integer> branchNames = new ArrayList<>();

                        for (BranchDTO branch : branches) {
                            branchNames.add(branch.getBranchId());
                        }

                        ArrayAdapter<Integer> branchAdapter =
                                new ArrayAdapter<>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        branchNames
                                );

                        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        branch.setAdapter(branchAdapter);
                    }

                } else {
                    Toast.makeText(requireContext(), "Failed to load filters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryFilterResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}