package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;

import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchViewHolder> {
    private List<DisplayBatchDTO> localData;

    public BatchAdapter(List<DisplayBatchDTO> data) {
        this.localData = data;
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_batches, parent, false);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BatchViewHolder holder, int position) {
        DisplayBatchDTO item = localData.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return localData != null ? localData.size() : 0;
    }

    public void updateData(List<DisplayBatchDTO> newData) {
        this.localData = newData;
        notifyDataSetChanged();
    }
}