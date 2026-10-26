package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.components.InventoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder> {

    public interface OnViewProductBatchesListener {
        void onViewProductBatches(DisplayInventoryDTO selectedProduct);
    }

    private final List<DisplayInventoryDTO> inventoryList = new ArrayList<>();
    private final OnViewProductBatchesListener listener;

    public InventoryAdapter(List<DisplayInventoryDTO> data, OnViewProductBatchesListener listener) {
        if (data != null) inventoryList.addAll(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        holder.bind(inventoryList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    /** Replaces the list with one page of results. */
    public void setItems(List<DisplayInventoryDTO> newData) {
        inventoryList.clear();
        if (newData != null) inventoryList.addAll(newData);
        notifyDataSetChanged();
    }

    /** Same as setItems (kept for older callers). */
    public void updateData(List<DisplayInventoryDTO> newData) {
        setItems(newData);
    }

    public void addData(List<DisplayInventoryDTO> newData) {
        if (newData != null && !newData.isEmpty()) {
            int start = inventoryList.size();
            inventoryList.addAll(newData);
            notifyItemRangeInserted(start, newData.size());
        }
    }
}
