package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestApprovalDetailsDTO;
import com.example.francosperfumemobile.components.RequestApprovalViewHolder;

import java.util.List;

// The request timeline
public class RequestApprovalAdapter extends RecyclerView.Adapter<RequestApprovalViewHolder> {

    List<DisplayRequestApprovalDetailsDTO> approvalRequestList;

    public RequestApprovalAdapter(List<DisplayRequestApprovalDetailsDTO> approvalRequestList) {
        this.approvalRequestList = approvalRequestList;
    }

    @NonNull
    @Override
    public RequestApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timeline, parent, false);
        return new RequestApprovalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestApprovalViewHolder holder, int position) {
        holder.bind(approvalRequestList.get(position), position == getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return approvalRequestList != null ? approvalRequestList.size() : 0;
    }
}
