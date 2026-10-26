package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DisplayDeliveryDTO;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.StatusStyle;

public class DeliveryViewHolder extends RecyclerView.ViewHolder {
    TextView deliveryId, deliveryUnitCount, deliveryBatchCount, deliveryDate,
            deliveryFromBranch, deliveryToBranch, deliveryStage, deliveryDirection;

    public DeliveryViewHolder(View view) {
        super(view);
        deliveryId = view.findViewById(R.id.text_view_delivery_id);
        deliveryUnitCount = view.findViewById(R.id.text_view_delivery_unit_count);
        deliveryBatchCount = view.findViewById(R.id.text_view_delivery_batch_count);
        deliveryDate = view.findViewById(R.id.text_view_delivery_date);
        deliveryFromBranch = view.findViewById(R.id.text_view_delivery_from_branch);
        deliveryToBranch = view.findViewById(R.id.text_view_delivery_to_branch);
        deliveryStage = view.findViewById(R.id.text_view_delivery_stage);
        deliveryDirection = view.findViewById(R.id.text_view_delivery_direction);
    }

    public void bind(DisplayDeliveryDTO item) {
        deliveryId.setText(item.getDeliveryDisplayId());
        deliveryUnitCount.setText(itemView.getResources().getQuantityString(R.plurals.unit_count, item.getTotalUnits(), item.getTotalUnits()));
        deliveryBatchCount.setText(itemView.getResources().getQuantityString(R.plurals.product_count, item.getItemCount(), item.getItemCount()));
        deliveryFromBranch.setText(orDash(item.getFromBranchName()));
        deliveryToBranch.setText(orDash(item.getToBranchName()));
        deliveryDate.setText(DateUtils.formatDate(item.getCreatedAt()));
        StatusStyle.apply(deliveryStage, item.getDeliveryStatus());

        if (item.getDirection() != null && !item.getDirection().isEmpty()) {
            StatusStyle.apply(deliveryDirection, item.getDirection());
            deliveryDirection.setVisibility(View.VISIBLE);
        } else {
            deliveryDirection.setVisibility(View.GONE);
        }

        itemView.setContentDescription(item.getDeliveryDisplayId() + ", " + StatusStyle.label(item.getDeliveryStatus())
                + ", from " + orDash(item.getFromBranchName()) + " to " + orDash(item.getToBranchName()));
    }

    private static String orDash(String value) {
        return value == null || value.isEmpty() ? "—" : value;
    }
}
