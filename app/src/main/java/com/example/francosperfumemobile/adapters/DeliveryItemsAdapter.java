package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.components.DeliveryItemsViewHolder;

import java.util.List;

public class DeliveryItemsAdapter extends RecyclerView.Adapter<DeliveryItemsViewHolder> {

    public interface OnViewDeliveryItemsListener {
        // TODO: Feel free to change the name of the variable of the DisplayInventoryBatchDTO to whatever suits it
        void onViewProductBatches(DeliveryItemDTO selectedDeliveryItems);
    }
    private List<DeliveryItemDTO> deliveryItems;
    private final DeliveryItemsAdapter.OnViewDeliveryItemsListener listener;

    public DeliveryItemsAdapter(List<DeliveryItemDTO> data, OnViewDeliveryItemsListener listener) {
        this.deliveryItems = data;
        this.listener = listener;
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