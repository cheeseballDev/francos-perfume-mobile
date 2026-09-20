package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    TextView cardRequestId, cardRequestUnitCount, cardRequestBatchCount, cardDeliveryId,
            cardRequestFromBranch, cardRequestToBranch, cardRequestStatus, cardRequestStage;

    public RequestViewHolder(View view) {
        super(view);
        cardRequestId = view.findViewById(R.id.text_view_request_id);
        cardRequestUnitCount = view.findViewById(R.id.text_view_request_unit_count);
        cardRequestBatchCount = view.findViewById(R.id.text_view_request_batch_count);
        cardDeliveryId = view.findViewById(R.id.text_view_request_delivery_id);
        cardRequestFromBranch = view.findViewById(R.id.text_view_request_from_branch);
        cardRequestToBranch = view.findViewById(R.id.text_view_to_branch);
        cardRequestStatus = view.findViewById(R.id.text_view_request_status);
        cardRequestStage = view.findViewById(R.id.text_view_request_details_direction);
    }

    public void bind(DisplayRequestDTO item) {
        cardRequestId.setText(item.getRequestDisplayId());
        // cardRequestUnitCount.setText("0"); // Not in DTO
        cardRequestBatchCount.setText(String.valueOf(item.getItemCount()));
        // cardDeliveryId.setText("N/A"); // Not in DTO
        cardRequestFromBranch.setText(item.getRequestedFrom());
        cardRequestToBranch.setText(item.getDeliveredTo());
        cardRequestStatus.setText(item.getRequestStatus());
        cardRequestStage.setText(item.getRequestStage());
    }
}
