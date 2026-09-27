package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.components.RequestViewHolder;
import com.example.francosperfumemobile.components.TimelineViewHolder;
import com.example.francosperfumemobile.helpers.TimelineUtils;

import java.util.List;

public class TimelineAdapter {

    List<DisplayRequestApprovalDetailsDTO> approvalRequestList;
    public TimelineAdapter(List<DisplayRequestApprovalDetailsDTO> approvalRequestList) {
        this.approvalRequestList =  approvalRequestList;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timeline, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        DisplayRequestApprovalDetailsDTO approvalRequest = approvalRequestList.get(position);
        holder.bind(approvalRequest);
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }

}
