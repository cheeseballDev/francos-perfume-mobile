package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.components.RequestedItemsViewHolder;

import java.util.List;

public class RequestedItemsAdapter extends RecyclerView.Adapter<RequestedItemsViewHolder> {
    List<DisplayRequestItemDetailsDTO> requestedItems;
    public RequestedItemsAdapter(List<DisplayRequestItemDetailsDTO> requestedItem) {
        this.requestedItems =  requestedItem;
    }

    @NonNull
    @Override
    public RequestedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_requested_products, parent, false);
        return new RequestedItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedItemsViewHolder holder, int position) {
        DisplayRequestItemDetailsDTO requestedItem = requestedItems.get(position);
        holder.bind(requestedItem);
    }

    @Override
    public int getItemCount() {
        return requestedItems != null ? requestedItems.size() : 0;
    }

}
