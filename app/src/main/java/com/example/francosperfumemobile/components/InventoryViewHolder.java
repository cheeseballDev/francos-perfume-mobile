package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.InventoryAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayInventoryDTO;
import com.example.francosperfumemobile.helpers.StatusStyle;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    ImageView imagePerfumeIcon;
    TextView productName, productId, productUnitCount, productBatchCount,
            productBranchLocation, productType, productGender, stockBadge;

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
        stockBadge = view.findViewById(R.id.text_view_inventory_stock_badge);
    }

    public void bind(@NonNull DisplayInventoryDTO display, InventoryAdapter.OnViewProductBatchesListener listener) {
        productName.setText(display.getProductName());
        productId.setText(display.getProductDisplayId());
        productUnitCount.setText(String.valueOf(display.getProductQuantity()));
        productBatchCount.setText(itemView.getResources().getQuantityString(
                R.plurals.batch_count, display.getProductBatchCount(), display.getProductBatchCount()));

        bindTag(productBranchLocation, display.getBranchName(),
                "WAREHOUSE".equalsIgnoreCase(display.getBranchName()) ? StatusStyle.Tone.PENDING : StatusStyle.Tone.SUCCESS,
                R.drawable.ic_map_pin);
        bindTag(productType, display.getProductType(), StatusStyle.Tone.NEUTRAL, 0);
        bindTag(productGender, display.getProductGender(), StatusStyle.Tone.NEUTRAL, 0);

        // Low / out of stock badge, same threshold as the website (statusUtils.js stockLevel)
        boolean warn = StatusStyle.applyStockBadge(stockBadge, display.getProductQuantity(), display.getStockWarning());
        productUnitCount.setTextColor(itemView.getContext().getColor(warn
                ? (display.getProductQuantity() <= 0 ? R.color.tone_danger_text : R.color.tone_pending_text)
                : R.color.fp_foreground));

        itemView.setContentDescription(display.getProductName() + ", " + display.getProductQuantity() + " units"
                + (display.getBranchName() != null ? ", " + display.getBranchName() : "")
                + (warn ? ", " + stockBadge.getText() : ""));
        itemView.setOnClickListener(v -> {
            if (listener != null) listener.onViewProductBatches(display);
        });

        String imageUrl = display.getProductImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.francos_perfume_logo)
                    .error(R.drawable.francos_perfume_logo)
                    .centerCrop()
                    .into(imagePerfumeIcon);
        } else {
            Glide.with(itemView.getContext()).clear(imagePerfumeIcon);
            imagePerfumeIcon.setImageResource(R.drawable.francos_perfume_logo);
        }
    }

    private static void bindTag(TextView view, String text, StatusStyle.Tone tone, int icon) {
        if (text == null || text.trim().isEmpty()) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        view.setText(text);
        StatusStyle.applyTone(view, tone, icon);
    }
}
