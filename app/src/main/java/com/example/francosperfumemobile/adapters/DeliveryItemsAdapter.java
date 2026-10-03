package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.components.DeliveryDetailsInboundViewHolder;
import com.example.francosperfumemobile.components.DeliveryOutboundViewHolder;

import java.util.List;

public class DeliveryItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface OnViewDeliveryItemsListener {
        // TODO: Feel free to change the name of the variable of the DisplayInventoryBatchDTO to whatever suits it
        void onViewProductBatches(DeliveryItemDTO selectedDeliveryItems);
    }
    private List<DeliveryItemDTO> deliveryItems;
    private final DeliveryItemsAdapter.OnViewDeliveryItemsListener listener;

    private static final int TYPE_INBOUND = 1;
    private static final int TYPE_OUTBOUND = 2;
    private final String direction;

    public DeliveryItemsAdapter(List<DeliveryItemDTO> data, String direction, OnViewDeliveryItemsListener listener) {
        this.deliveryItems = data;
        this.direction = direction;
        this.listener = listener;
    }

    public int getItemViewType(int position) {
        return direction.equals("INBOUND") ? TYPE_INBOUND : TYPE_OUTBOUND;
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
            return new DeliveryOutboundViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeliveryItemDTO item = deliveryItems.get(position);

        if (holder instanceof DeliveryDetailsInboundViewHolder) {
            ((DeliveryDetailsInboundViewHolder) holder).bind(item);
        } else if (holder instanceof DeliveryOutboundViewHolder) {
            ((DeliveryOutboundViewHolder) holder).bind(item);
        }


        // todo: add the listeners for the checkbox and text field for the deliveryinboundviewholder

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewProductBatches(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryItems != null ? deliveryItems.size() : 0;
    }

    public void updateData(List<DeliveryItemDTO> newData) {
        this.deliveryItems = newData;
        //notifyDataSetChanged();
    }
}