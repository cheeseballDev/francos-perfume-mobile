package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestDTO;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.StatusStyle;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    TextView requestId, requestUnitCount, deliveryId, requestDate,
            requestFromBranch, requestToBranch, requestStatus, requestStage, requestDirection;

    public RequestViewHolder(View view) {
        super(view);
        requestId = view.findViewById(R.id.text_view_request_id);
        deliveryId = view.findViewById(R.id.text_view_request_delivery_id);
        requestUnitCount = view.findViewById(R.id.text_view_request_unit_count);
        requestDate = view.findViewById(R.id.text_view_request_date);
        requestFromBranch = view.findViewById(R.id.text_view_request_from_branch);
        requestToBranch = view.findViewById(R.id.text_view_request_to_branch);
        requestStatus = view.findViewById(R.id.text_view_request_status);
        requestStage = view.findViewById(R.id.text_view_request_stage);
        requestDirection = view.findViewById(R.id.text_view_request_direction);
    }

    public void bind(DisplayRequestDTO item) {
        requestId.setText(item.getRequestDisplayId());
        requestUnitCount.setText(itemView.getResources().getQuantityString(R.plurals.item_count, item.getItemCount(), item.getItemCount()));
        requestFromBranch.setText(orDash(item.getRequestedFrom()));
        requestToBranch.setText(orDash(item.getDeliveredTo()));
        StatusStyle.apply(requestStatus, item.getRequestStatus());

        String stage = item.getRequestStage();
        requestStage.setText(stage == null || stage.isEmpty() ? "—" : StatusStyle.label(stage));

        if (item.getRequestDirection() != null && !item.getRequestDirection().isEmpty()) {
            StatusStyle.apply(requestDirection, item.getRequestDirection());
            requestDirection.setVisibility(View.VISIBLE);
        } else {
            requestDirection.setVisibility(View.GONE);
        }

        requestDate.setText(DateUtils.formatDate(item.getRequestDateSubmitted()));

        String delivery = item.getDeliveryDisplayId();
        if (delivery != null && !delivery.isEmpty()) {
            deliveryId.setText(delivery);
            deliveryId.setVisibility(View.VISIBLE);
        } else {
            deliveryId.setVisibility(View.GONE);
        }

        itemView.setContentDescription(item.getRequestDisplayId() + ", " + StatusStyle.label(item.getRequestStatus())
                + ", from " + orDash(item.getRequestedFrom()) + " to " + orDash(item.getDeliveredTo()));
    }

    private static String orDash(String value) {
        return value == null || value.isEmpty() ? "—" : value;
    }
}
