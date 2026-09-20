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
    TextView textProductName, textProductId, textProductUnitCount, textProductBatchCount,
            textProductBranchLocation, textProductType, textProductGender;


    public InventoryViewHolder(View view) {
        super(view);
        imagePerfumeIcon = view.findViewById(R.id.image_view_inventory_icon);
        textProductName = view.findViewById(R.id.text_view_inventory_product_name);
        textProductId = view.findViewById(R.id.text_view_inventory_product_id);
        textProductUnitCount = view.findViewById(R.id.text_view_inventory_unit_count);
        textProductBatchCount = view.findViewById(R.id.text_view_inventory_batch_count);
        textProductBranchLocation = view.findViewById(R.id.text_view_inventory_branch_location);
        textProductType = view.findViewById(R.id.text_view_inventory_product_type);
        textProductGender = view.findViewById(R.id.text_view_inventory_gender_type);

    }

    public void bind(DisplayInventoryDTO display, InventoryAdapter.OnViewProductBatchesListener listener) {
        textProductName.setText(display.getProductName());
        textProductId.setText(display.getProductDisplayId());
        textProductUnitCount.setText(String.valueOf(display.getProductQuantity()));
        textProductBatchCount.setText(String.valueOf(display.getProductBatchCount()));
        textProductBranchLocation.setText(display.getBranchName());
        textProductType.setText(display.getProductType());
        textProductGender.setText(display.getProductGender());

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