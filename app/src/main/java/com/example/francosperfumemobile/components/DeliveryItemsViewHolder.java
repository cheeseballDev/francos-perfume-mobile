package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;

public class DeliveryItemsViewHolder extends RecyclerView.ViewHolder {

    ImageView deliveryItemsImage;
    TextView deliveryItemsProductName, deliveryItemsProductId, deliveryItemsUnitCount;

    public DeliveryItemsViewHolder(View view) {
        super(view);
        deliveryItemsImage = view.findViewById(R.id.image_view_delivery_items_icon);
        deliveryItemsProductName = view.findViewById(R.id.text_view_delivery_items_product_name);
        deliveryItemsProductId = view.findViewById(R.id.text_view_delivery_items_product_id);
        deliveryItemsUnitCount = view.findViewById(R.id.text_view_delivery_items_unit_count);
    }

    public void bind(DeliveryItemDTO item) {
        if (item == null) return;

        deliveryItemsProductName.setText(item.getProductName());
        deliveryItemsProductId.setText(item.getProductId());
        deliveryItemsUnitCount.setText(String.format("%d units", item.getQuantity()));

    }
}