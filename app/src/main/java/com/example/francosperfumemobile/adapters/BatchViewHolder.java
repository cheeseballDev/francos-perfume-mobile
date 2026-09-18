package com.example.francosperfumemobile.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;

public class BatchViewHolder extends RecyclerView.ViewHolder {
    TextView cardBatchId, cardBatchQuantity, cardBatchDateReceived, cardBatchTargetDate;

    public BatchViewHolder(View view) {
        super(view);
        cardBatchId = view.findViewById(R.id.card_batch_id);
        cardBatchQuantity = view.findViewById(R.id.card_delivery_unit_count);
        cardBatchDateReceived = view.findViewById(R.id.card_batch_date_received);
        cardBatchTargetDate = view.findViewById(R.id.card_batch_target_date);
    }

    public void bind(DisplayBatchDTO item) {
        cardBatchId.setText(item.getBatchDisplayId());
        cardBatchQuantity.setText(String.valueOf(item.getQuantity()));
        cardBatchDateReceived.setText(item.getCreatedAt());
        cardBatchTargetDate.setText(item.getExpiryDate());
    }
}