package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.components.DeliveryItemsViewHolder;

import java.util.List;

public class DeliveryItemsAdapter extends RecyclerView.Adapter<DeliveryItemsViewHolder> {

    private List<DeliveryItemDTO> deliveryItems;

    public DeliveryItemsAdapter(List<DeliveryItemDTO> data) {
        this.deliveryItems = data;
    }

    @NonNull
    @Override
    public DeliveryItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_delivery_items, parent, false);
        return new DeliveryItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryItemsViewHolder holder, int position) {
        DeliveryItemDTO item = deliveryItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return deliveryItems != null ? deliveryItems.size() : 0;
    }

    public void updateData(List<DeliveryItemDTO> newData) {
        this.deliveryItems = newData;
        //notifyDataSetChanged();
    }
}