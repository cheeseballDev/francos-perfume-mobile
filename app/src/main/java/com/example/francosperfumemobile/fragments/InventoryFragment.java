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

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.InventoryBatchListActivity;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private final List<DisplayInventoryDTO> inventoryList = new ArrayList<>();

    public InventoryFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        InventoryAdapter adapter = new InventoryAdapter(inventoryList, selectedProduct -> {
            Intent intent = InventoryBatchListActivity.newIntent(requireContext(), selectedProduct.getProductId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}