package com.example.francosperfumemobile.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    ImageView imagePerfumeIcon;
    TextView textProductName, textProductId, textProductUnitCount, textProductBatchCount,
            textProductBranchLocation, textProductType, textProductGender;
    public InventoryViewHolder(View view) {
        super(view);
        imagePerfumeIcon = view.findViewById(R.id.card_inventory_icon);
        textProductName = view.findViewById(R.id.card_inventory_product_name);
        textProductId = view.findViewById(R.id.card_inventory_product_id);
        textProductUnitCount = view.findViewById(R.id.card_inventory_unit_count);
        textProductBatchCount = view.findViewById(R.id.card_inventory_batch_count);
        textProductBranchLocation = view.findViewById(R.id.card_inventory_branch_location);
        textProductType = view.findViewById(R.id.card_inventory_product_type);
        textProductGender = view.findViewById(R.id.card_inventory_gender_type);

    }

    public void bind(DisplayInventoryDTO item) {
        textProductName.setText(item.getProductName());
        textProductId.setText(item.getProductDisplayId());
        textProductUnitCount.setText(String.valueOf(item.getProductQuantity()));
        textProductBatchCount.setText(String.valueOf(item.getProductBatchCount()));
        textProductBranchLocation.setText(item.getBranchName());
        textProductType.setText(item.getProductType());
        textProductGender.setText(item.getProductGender());

        // TODO: Add images
        /*
        Glide.with(itemView.getContext())
                .load(item.getProductImageUrl())
                .placeholder(R.drawable.francos_perfume_logo)
                .into(imagePerfumeIcon);
         */
    }
}