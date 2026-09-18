package com.example.francosperfumemobile.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;

public class DeliveryViewHolder extends RecyclerView.ViewHolder {
    TextView cardDeliveryId, cardDeliveryUnitCount, cardDeliveryBatchCount, cardDeliveryDirection, cardDeliveryStage;

    public DeliveryViewHolder(View view) {
        super(view);
        cardDeliveryId = view.findViewById(R.id.card_delivery_id);
        cardDeliveryUnitCount = view.findViewById(R.id.card_delivery_unit_count);
        cardDeliveryBatchCount = view.findViewById(R.id.card_delivery_batch_count);
        cardDeliveryDirection = view.findViewById(R.id.card_delivery_direction);
        cardDeliveryStage = view.findViewById(R.id.card_delivery_stage);
    }

    public void bind(DisplayDeliveryDTO item) {
        cardDeliveryId.setText(item.getDeliveryDisplayId());
        cardDeliveryUnitCount.setText(String.valueOf(item.getTotalUnits()));
        cardDeliveryBatchCount.setText(String.valueOf(item.getItemCount()));
        cardDeliveryDirection.setText(item.getDirection());
        cardDeliveryStage.setText(item.getDeliveryStatus());
    }
}
