package com.example.francosperfumemobile.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    TextView cardRequestId, cardRequestUnitCount, cardRequestBatchCount, cardDeliveryId,
            cardRequestDirection, cardRequestStatus, cardRequestStage;

    public RequestViewHolder(View view) {
        super(view);
        cardRequestId = view.findViewById(R.id.card_request_id);
        cardRequestUnitCount = view.findViewById(R.id.card_request_unit_count);
        cardRequestBatchCount = view.findViewById(R.id.card_request_batch_count);
        cardDeliveryId = view.findViewById(R.id.card_delivery_id);
        cardRequestDirection = view.findViewById(R.id.card_request_direction);
        cardRequestStatus = view.findViewById(R.id.card_request_status);
        cardRequestStage = view.findViewById(R.id.card_request_stage);
    }

    public void bind(DisplayRequestDTO item) {
        cardRequestId.setText(item.getRequestDisplayId());
        // cardRequestUnitCount.setText("0"); // Not in DTO
        cardRequestBatchCount.setText(String.valueOf(item.getItemCount()));
        // cardDeliveryId.setText("N/A"); // Not in DTO
        String direction = item.getRequestedFrom() + " -> " + item.getDeliveredTo();
        cardRequestDirection.setText(direction);
        cardRequestStatus.setText(item.getRequestStatus());
        cardRequestStage.setText(item.getRequestStage());
    }
}
