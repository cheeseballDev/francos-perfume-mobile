package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.components.DeliveryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryViewHolder> {
    private final List<DisplayDeliveryDTO> deliveryList = new ArrayList<>();
    private final DeliveryAdapter.OnDeliveryClickListener listener;

    public interface OnDeliveryClickListener {
        void onDeliveryClick(DisplayDeliveryDTO delivery);
    }

    public DeliveryAdapter(List<DisplayDeliveryDTO> data, DeliveryAdapter.OnDeliveryClickListener listener) {
        if (data != null) deliveryList.addAll(data);
        this.listener = listener;
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
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDeliveryClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public void setItems(List<DisplayDeliveryDTO> newData) {
        deliveryList.clear();
        if (newData != null) deliveryList.addAll(newData);
        notifyDataSetChanged();
    }

    public void updateData(List<DisplayDeliveryDTO> newData) {
        setItems(newData);
    }

    public void addData(List<DisplayDeliveryDTO> newData) {
        if (newData != null && !newData.isEmpty()) {
            int start = deliveryList.size();
            deliveryList.addAll(newData);
            notifyItemRangeInserted(start, newData.size());
        }
    }
}
