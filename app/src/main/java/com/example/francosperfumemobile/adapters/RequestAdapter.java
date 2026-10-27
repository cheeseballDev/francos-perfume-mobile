package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.components.RequestViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private final List<DisplayRequestDTO> requestList = new ArrayList<>();
    private final OnRequestClickListener listener;

    public interface OnRequestClickListener {
        void onRequestClick(DisplayRequestDTO request);
    }

    public RequestAdapter(List<DisplayRequestDTO> data, OnRequestClickListener listener) {
        if (data != null) requestList.addAll(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        DisplayRequestDTO item = requestList.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRequestClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setItems(List<DisplayRequestDTO> newData) {
        requestList.clear();
        if (newData != null) requestList.addAll(newData);
        notifyDataSetChanged();
    }

    public void updateData(List<DisplayRequestDTO> newData) {
        setItems(newData);
    }

    public void addData(List<DisplayRequestDTO> newData) {
        if (newData != null && !newData.isEmpty()) {
            int start = requestList.size();
            requestList.addAll(newData);
            notifyItemRangeInserted(start, newData.size());
        }
    }
}
