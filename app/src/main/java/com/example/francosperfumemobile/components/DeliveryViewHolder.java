package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;

public class DeliveryViewHolder extends RecyclerView.ViewHolder {
    TextView deliveryId, deliveryUnitCount, deliveryBatchCount,
            deliveryFromBranch, deliveryToBranch, deliveryStage, deliveryDirection;

    public DeliveryViewHolder(View view) {
        super(view);
        deliveryId = view.findViewById(R.id.text_view_request_delivery_id);
        deliveryUnitCount = view.findViewById(R.id.text_view_delivery_unit_count);
        deliveryBatchCount = view.findViewById(R.id.text_view_delivery_batch_count);
        deliveryFromBranch = view.findViewById(R.id.text_view_delivery_from_branch);
        deliveryToBranch = view.findViewById(R.id.text_view_delivery_to_branch);
        deliveryStage = view.findViewById(R.id.text_view_delivery_stage);
        deliveryDirection = view.findViewById(R.id.text_view_delivery_direction);
    }

    public void bind(DisplayDeliveryDTO item) {
        deliveryId.setText(item.getDeliveryDisplayId());
        deliveryUnitCount.setText(String.valueOf(item.getTotalUnits()));
        deliveryBatchCount.setText(String.valueOf(item.getItemCount()));
        deliveryFromBranch.setText(item.getFromBranchName());
        deliveryToBranch.setText(item.getToBranchName());
        deliveryStage.setText(item.getDeliveryStatus());

        // TODO: Add conditional statement to get user's branch and then setting the deliveryDirection to inbound/outbound
        // deliveryDirection.setText(userBranch == getFromBranchName() ? "OUTBOUND" : "INBOUND") or something like that wherein userBranch can be a sharedpref
    }
}
