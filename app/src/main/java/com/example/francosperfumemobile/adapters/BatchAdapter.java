package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.components.BatchViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchViewHolder> {

    public interface OnEditBatchListener {
        void onEditBatchClick(DisplayBatchDTO item);
    }

    private final List<DisplayBatchDTO> batchList = new ArrayList<>();
    @Nullable
    private final OnEditBatchListener listener;

    public BatchAdapter(List<DisplayBatchDTO> data) {
        this(data, null);
    }

    /** @param listener null hides the Edit button */
    public BatchAdapter(List<DisplayBatchDTO> data, @Nullable OnEditBatchListener listener) {
        if (data != null) batchList.addAll(data);
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
        holder.bind(batchList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

    public void updateData(List<DisplayBatchDTO> newData) {
        batchList.clear();
        if (newData != null) batchList.addAll(newData);
        notifyDataSetChanged();
    }
}
