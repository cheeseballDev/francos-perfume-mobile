package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryViewHolder> {
    private List<DisplayDeliveryDTO> localData;

    public DeliveryAdapter(List<DisplayDeliveryDTO> data) {
        this.localData = data;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        DisplayDeliveryDTO item = localData.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return localData != null ? localData.size() : 0;
    }

    public void updateData(List<DisplayDeliveryDTO> newData) {
        this.localData = newData;
        notifyDataSetChanged();
    }
}
