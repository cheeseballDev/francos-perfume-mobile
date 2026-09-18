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

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.adapters.RequestAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {

    private final List<DisplayRequestDTO> requestList = new ArrayList<>();

    public RequestFragment() {
        // constructor for whatever
    }

    public static RequestFragment newInstance(String param1, String param2) {
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
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RequestAdapter adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);
    }
}