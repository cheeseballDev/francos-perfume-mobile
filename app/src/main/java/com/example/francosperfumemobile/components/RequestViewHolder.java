package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    TextView requestId, requestUnitCount, requestBatchCount, deliveryId,
            requestFromBranch, requestToBranch, requestStatus, requestStage;

    public RequestViewHolder(View view) {
        super(view);
        requestId = view.findViewById(R.id.text_view_request_id);
        requestUnitCount = view.findViewById(R.id.text_view_request_unit_count);
        requestBatchCount = view.findViewById(R.id.text_view_request_batch_count);
        deliveryId = view.findViewById(R.id.text_view_request_delivery_id);
        requestFromBranch = view.findViewById(R.id.text_view_request_from_branch);
        requestToBranch = view.findViewById(R.id.text_view_request_to_branch);
        requestStatus = view.findViewById(R.id.text_view_request_status);
        requestStage = view.findViewById(R.id.text_view_request_details_direction);
    }

    public void bind(DisplayRequestDTO item) {
        requestId.setText(item.getRequestDisplayId());
        requestBatchCount.setText(String.valueOf(item.getItemCount()));
        // cardDeliveryId.setText("N/A"); // Not in DTO
        //todo: add deliveryId
        requestUnitCount.setText(item.getItemCount());
        requestFromBranch.setText(item.getRequestedFrom());
        requestToBranch.setText(item.getDeliveredTo());
        requestStatus.setText(item.getRequestStatus());
        requestStage.setText(item.getRequestStage());
    }
}
