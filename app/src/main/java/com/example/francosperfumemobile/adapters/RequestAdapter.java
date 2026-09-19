package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.components.RequestViewHolder;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private List<DisplayRequestDTO> requestList;

    public RequestAdapter(List<DisplayRequestDTO> data) {
        requestList = data;
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
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }

    public void updateData(List<DisplayRequestDTO> newData) {
        this.requestList = newData;
        notifyDataSetChanged();
    }
}
