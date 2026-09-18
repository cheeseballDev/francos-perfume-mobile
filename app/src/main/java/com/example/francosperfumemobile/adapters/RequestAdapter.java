package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private List<DisplayRequestDTO> localData;

    public RequestAdapter(List<DisplayRequestDTO> data) {
        localData = data;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        DisplayRequestDTO item = localData.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return localData != null ? localData.size() : 0;
    }

    public void updateData(List<DisplayRequestDTO> newData) {
        this.localData = newData;
        notifyDataSetChanged();
    }
}
