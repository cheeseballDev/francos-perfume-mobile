package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.RequestSummaryDTO;

public class RequestedItemsViewHolder extends RecyclerView.ViewHolder {
    TextView productName, productAvailableQty, productRequestedQty, productId;

    public RequestedItemsViewHolder(View view) {
        super(view);
        productName = view.findViewById(R.id.text_view_requested_product_name);
        productId = view.findViewById(R.id.text_view_inventory_product_id);
        productAvailableQty = view.findViewById(R.id.text_view_requested_product_available_amount);
        productRequestedQty = view.findViewById(R.id.text_view_requested_amount);
    }

    public void bind(DisplayRequestItemDetailsDTO item) {
        productName.setText(item.getProductName());
        productId.setText(item.getProductDisplayId());
        // TODO: Add total units
        //productAvailableQty.setText(String.valueOf(item.getAvailableQty()));
        productRequestedQty.setText(String.valueOf(item.getRequestedQty()));
    }
}
