package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.components.BatchViewHolder;

import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchViewHolder> {

    public interface OnEditBatchListener {
        void onEditBatchClick(DisplayBatchDTO item);
    }

    private List<DisplayBatchDTO> batchList;
    private final OnEditBatchListener listener;

    public BatchAdapter(List<DisplayBatchDTO> data, OnEditBatchListener listener) {
        this.batchList = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_batches, parent, false);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BatchViewHolder holder, int position) {
        DisplayBatchDTO item = batchList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return batchList != null ? batchList.size() : 0;
    }

    public void updateData(List<DisplayBatchDTO> newData) {
        this.batchList = newData;
        //notifyDataSetChanged();
    }
}