package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.components.DeliveryDetailsInboundViewHolder;
import com.example.francosperfumemobile.components.DeliveryDetailsOutboundViewHolder;

import java.util.List;

public class DeliveryItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemChangedListener {
        void onItemChanged(DeliveryItemDTO item);
    }

    private List<DeliveryItemDTO> deliveryItems;
    private final boolean isInbound;
    private OnItemChangedListener itemChangedListener;

    private static final int TYPE_INBOUND = 1;
    private static final int TYPE_OUTBOUND = 2;

    public DeliveryItemsAdapter(List<DeliveryItemDTO> data, boolean isInbound) {
        this.deliveryItems = data;
        this.isInbound = isInbound;
    }

    public void setOnItemChangedListener(OnItemChangedListener listener) {
        this.itemChangedListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return isInbound ? TYPE_INBOUND : TYPE_OUTBOUND;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_INBOUND) {
            View view = inflater.inflate(R.layout.card_delivery_inbound_items, parent, false);
            return new DeliveryDetailsInboundViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.card_delivery_outbound_items, parent, false);
            return new DeliveryDetailsOutboundViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeliveryItemDTO item = deliveryItems.get(position);

        if (holder instanceof DeliveryDetailsInboundViewHolder) {
            ((DeliveryDetailsInboundViewHolder) holder).bind(item, updatedItem -> {
                if (itemChangedListener != null) {
                    itemChangedListener.onItemChanged(updatedItem);
                }
            });
        } else if (holder instanceof DeliveryDetailsOutboundViewHolder) {
            ((DeliveryDetailsOutboundViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return deliveryItems != null ? deliveryItems.size() : 0;
    }

    public void updateData(List<DeliveryItemDTO> newData) {
        this.deliveryItems = newData;
        notifyDataSetChanged();
    }
}