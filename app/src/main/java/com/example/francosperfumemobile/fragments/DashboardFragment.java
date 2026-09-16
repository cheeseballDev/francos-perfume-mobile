package com.example.francosperfumemobile.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.components.DashboardCardView;

public class DashboardFragment extends Fragment {

    private DashboardCardView inventoryCard, requestCard, dispatchCard, deliveryCard;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       initializeUI(view);

        //TODO: ADD DATA HERE. PLEASE CREATE A HOOK/SERVICE THAT GETS ALL OF THE DATA INSTEAD OF PUTTING THE LOGIC HERE.
        if (inventoryCard != null) {
            inventoryCard.setData("Total Inventory", "1,250", "12 items low stock");
        }
        if (requestCard != null) {
            requestCard.setData("Pending Requests", "5", "Action Required");
        }
        if (dispatchCard != null) {
            dispatchCard.setData("For Dispatch", "2", "Ready for shipping");
        }
        if (deliveryCard != null) {
            deliveryCard.setData("Deliveries", "8", "In transit");
        }
    }

    private void initializeUI(View view) {
        inventoryCard = view.findViewById(R.id.dashboard_inventory_card);
        requestCard = view.findViewById(R.id.dashboard_requests_card);
        dispatchCard = view.findViewById(R.id.dashboard_dispatch_card);
        deliveryCard = view.findViewById(R.id.dashboard_delivery_card);
    }


}