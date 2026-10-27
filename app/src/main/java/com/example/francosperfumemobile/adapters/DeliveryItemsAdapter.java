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

import java.util.ArrayList;
import java.util.List;

/**
 * Items of a delivery. In receive mode each row is editable (received?, quantity, reason);
 * otherwise rows are read only. What the user types is stored on the item itself
 * (DeliveryItemDTO.receivedQty / receiveRemark), never in the requested quantity.
 */
public class DeliveryItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemChangedListener {
        void onItemChanged(DeliveryItemDTO item);
    }

    private final List<DeliveryItemDTO> deliveryItems = new ArrayList<>();
    private final boolean receiveMode;
    private OnItemChangedListener itemChangedListener;

    private static final int TYPE_RECEIVE = 1;
    private static final int TYPE_READ_ONLY = 2;

    /**
     * @param isInbound   kept for older callers; receiving needs isInbound and !isCompleted
     * @param isCompleted when true the rows are read only
     */
    public DeliveryItemsAdapter(List<DeliveryItemDTO> data, boolean isInbound, boolean isCompleted) {
        if (data != null) deliveryItems.addAll(data);
        this.receiveMode = isInbound && !isCompleted;
    }

    public void setOnItemChangedListener(OnItemChangedListener listener) {
        this.itemChangedListener = listener;
    }

    public boolean isReceiveMode() {
        return receiveMode;
    }

    @Override
    public int getItemViewType(int position) {
        return receiveMode ? TYPE_RECEIVE : TYPE_READ_ONLY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_RECEIVE) {
            View view = inflater.inflate(R.layout.card_delivery_inbound_items, parent, false);
            return new DeliveryDetailsInboundViewHolder(view);
        }
        View view = inflater.inflate(R.layout.card_delivery_outbound_items, parent, false);
        return new DeliveryDetailsOutboundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeliveryItemDTO item = deliveryItems.get(position);
        if (holder instanceof DeliveryDetailsInboundViewHolder) {
            ((DeliveryDetailsInboundViewHolder) holder).bind(item, false, updatedItem -> {
                if (itemChangedListener != null) itemChangedListener.onItemChanged(updatedItem);
            });
        } else if (holder instanceof DeliveryDetailsOutboundViewHolder) {
            ((DeliveryDetailsOutboundViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return deliveryItems.size();
    }

    public List<DeliveryItemDTO> getItems() {
        return deliveryItems;
    }

    public void updateData(List<DeliveryItemDTO> newData) {
        deliveryItems.clear();
        if (newData != null) deliveryItems.addAll(newData);
        notifyDataSetChanged();
    }
}
