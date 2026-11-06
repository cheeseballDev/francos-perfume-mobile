package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.helpers.StatusStyle;

/** Read-only delivery item (outbound, or anything not being received right now). */
public class DeliveryDetailsOutboundViewHolder extends RecyclerView.ViewHolder {

    ImageView deliveryItemsImage;
    TextView deliveryItemsProductName, deliveryItemsProductId, deliveryItemsUnitCount, deliveryItemsStatus, deliveryItemsReceived;

    public DeliveryDetailsOutboundViewHolder(View view) {
        super(view);
        deliveryItemsImage = view.findViewById(R.id.image_view_delivery_outbound_icon);
        deliveryItemsProductName = view.findViewById(R.id.text_view_delivery_outbound_product_name);
        deliveryItemsProductId = view.findViewById(R.id.text_view_delivery_outbound_product_id);
        deliveryItemsUnitCount = view.findViewById(R.id.text_view_delivery_outbound_unit_count);
        deliveryItemsStatus = view.findViewById(R.id.text_view_delivery_outbound_status);
        deliveryItemsReceived = view.findViewById(R.id.text_view_delivery_outbound_received);
    }

    public void bind(DeliveryItemDTO item) {
        if (item == null) return;
        deliveryItemsProductName.setText(item.getProductName());
        deliveryItemsProductId.setText(itemView.getContext().getString(R.string.product_number, item.getProductId()));
        deliveryItemsUnitCount.setText(itemView.getResources().getQuantityString(R.plurals.unit_count, item.getQuantity(), item.getQuantity()));

        String status = item.getStatus();
        if ("RECEIVED".equalsIgnoreCase(status == null ? "" : status.trim())) {
            deliveryItemsReceived.setText(itemView.getContext().getString(R.string.received_of_format,
                    item.getServerReceivedQty(), item.getQuantity()));
            deliveryItemsReceived.setVisibility(View.VISIBLE);
        } else {
            deliveryItemsReceived.setVisibility(View.GONE);
        }

        if (status != null && !status.trim().isEmpty()) {
            StatusStyle.apply(deliveryItemsStatus, status);
            deliveryItemsStatus.setVisibility(View.VISIBLE);
        } else {
            deliveryItemsStatus.setVisibility(View.GONE);
        }
    }
}
