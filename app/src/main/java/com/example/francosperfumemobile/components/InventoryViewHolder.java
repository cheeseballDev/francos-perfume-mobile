package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    ImageView imagePerfumeIcon;
    TextView productName, productId, productUnitCount, productBatchCount,
            productBranchLocation, productType, productGender;


    public InventoryViewHolder(View view) {
        super(view);
        imagePerfumeIcon = view.findViewById(R.id.image_view_inventory_icon);
        productName = view.findViewById(R.id.text_view_inventory_product_name);
        productId = view.findViewById(R.id.text_view_inventory_product_id);
        productUnitCount = view.findViewById(R.id.text_view_inventory_unit_count);
        productBatchCount = view.findViewById(R.id.text_view_inventory_batch_count);
        productBranchLocation = view.findViewById(R.id.text_view_inventory_branch_location);
        productType = view.findViewById(R.id.text_view_inventory_product_type);
        productGender = view.findViewById(R.id.text_view_inventory_gender_type);
    }

    public void bind(DisplayInventoryDTO display, InventoryAdapter.OnViewProductBatchesListener listener) {
        productName.setText(display.getProductName());
        productId.setText(display.getProductDisplayId());
        productUnitCount.setText(String.valueOf(display.getProductQuantity()));
        productBatchCount.setText(String.valueOf(display.getProductBatchCount()));
        productBranchLocation.setText(display.getBranchName());
        productType.setText(display.getProductType());
        productGender.setText(display.getProductGender());

        itemView.setOnClickListener(v -> {
            listener.onViewProductBatches(display);
        });

        // TODO: Add images
        /*
        Glide.with(itemView.getContext())
                .load(item.getProductImageUrl())
                .placeholder(R.drawable.francos_perfume_logo)
                .into(imagePerfumeIcon);
         */
    }
}