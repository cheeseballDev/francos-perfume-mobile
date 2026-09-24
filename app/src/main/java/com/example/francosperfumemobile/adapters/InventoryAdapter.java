package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.components.InventoryViewHolder;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder> {

    public interface OnViewProductBatchesListener {
        // TODO: Feel free to change the name of the variable of the DisplayInventoryBatchDTO to whatever suits it
        void onViewProductBatches(DisplayInventoryDTO selectedProduct);
    }
    private List<DisplayInventoryDTO> inventoryList;

    private final OnViewProductBatchesListener listener;

    public InventoryAdapter(List<DisplayInventoryDTO> data, OnViewProductBatchesListener listener) {
        this.inventoryList = data;
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
        DisplayInventoryDTO display = inventoryList.get(position);
        holder.bind(display, listener);
    }

    @Override
    public int getItemCount() {
        return inventoryList != null ? inventoryList.size() : 0;
    }

    public void addData(List<DisplayInventoryDTO> newItems) {
        if (newItems != null && !newItems.isEmpty()) {
            int startPosition = this.inventoryList.size();
            this.inventoryList.addAll(newItems);
            notifyItemRangeInserted(startPosition, newItems.size());
        }
    }
    public void updateData(List<DisplayInventoryDTO> newData) {
        this.inventoryList = newData;
        //notifyDataSetChanged();
    }
}
