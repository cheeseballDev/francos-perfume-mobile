package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder> {
    private List<DisplayInventoryDTO> localData;

    public InventoryAdapter(List<DisplayInventoryDTO> data) {
        localData = data;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        DisplayInventoryDTO item = localData.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return localData != null ? localData.size() : 0;
    }

    public void updateData(List<DisplayInventoryDTO> newData) {
        this.localData = newData;
        notifyDataSetChanged();
    }
}
