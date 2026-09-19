package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.BatchAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;

public class BatchViewHolder extends RecyclerView.ViewHolder {
    TextView cardBatchId, cardBatchQuantity, cardBatchDateReceived, cardBatchTargetDate;
    AppCompatButton buttonEditBatch;

    int batchId;

    public BatchViewHolder(View view) {
        super(view);
        initializeCardUI(view);

    }

    private void initializeCardUI(View view) {
        cardBatchId = view.findViewById(R.id.text_view_batch_id);
        cardBatchQuantity = view.findViewById(R.id.text_view_delivery_unit_count);
        cardBatchDateReceived = view.findViewById(R.id.edit_text_batch_date_received);
        cardBatchTargetDate = view.findViewById(R.id.text_view_batch_target_date);
        buttonEditBatch = view.findViewById(R.id.button_edit_batch);
    }

    public void bind(DisplayBatchDTO item, BatchAdapter.OnEditBatchListener listener) {
        buttonEditBatch.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditBatchClick(item);
            }
        });

        batchId = item.getBatchId();
        cardBatchId.setText(item.getBatchDisplayId());
        cardBatchQuantity.setText(String.valueOf(item.getQuantity()));
        cardBatchDateReceived.setText(item.getCreatedAt());
        cardBatchTargetDate.setText(item.getExpiryDate());
    }
}