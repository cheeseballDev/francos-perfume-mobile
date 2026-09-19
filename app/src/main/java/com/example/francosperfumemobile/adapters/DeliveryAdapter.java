package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.components.DeliveryViewHolder;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryViewHolder> {
    private List<DisplayDeliveryDTO> deliveryList;

    public DeliveryAdapter(List<DisplayDeliveryDTO> data) {
        this.deliveryList = data;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        DisplayDeliveryDTO item = deliveryList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return deliveryList != null ? deliveryList.size() : 0;
    }

    public void updateData(List<DisplayDeliveryDTO> newData) {
        this.deliveryList = newData;
        notifyDataSetChanged();
    }
}
