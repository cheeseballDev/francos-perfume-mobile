package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.ApproveRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.components.RequestedItemsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Products in a request. When the user may approve, each row has an "approve" checkbox and an
 * approved quantity (website: requestedProductsColumns + handleApproveToggle/handleQtyChange):
 *  - unticking sets the quantity to 0, ticking sets it back to the requested amount
 *  - the quantity is kept between 0 and the requested amount; 0 unticks the row
 * State is kept per requestItemId, so recycled rows never write into the wrong item.
 */
public class RequestedItemsAdapter extends RecyclerView.Adapter<RequestedItemsViewHolder> {

    /** Approval choice for one product. */
    public static final class ItemApproval {
        public boolean approved = true;
        public int quantity;

        ItemApproval(int quantity) {
            this.quantity = quantity;
        }
    }

    public interface OnApprovalsChanged {
        void onApprovalsChanged();
    }

    private final List<DisplayRequestItemDetailsDTO> requestedItems = new ArrayList<>();
    private final Map<Integer, ItemApproval> approvals = new HashMap<>();
    private boolean isReadOnly = true;
    @Nullable
    private OnApprovalsChanged changeListener;

    public RequestedItemsAdapter(List<DisplayRequestItemDetailsDTO> requestedItems) {
        if (requestedItems != null) setItems(requestedItems);
    }

    public void setOnApprovalsChanged(@Nullable OnApprovalsChanged listener) {
        this.changeListener = listener;
    }

    /** New data from the server: start every product as "approve the full amount". */
    public void setItems(List<DisplayRequestItemDetailsDTO> items) {
        requestedItems.clear();
        approvals.clear();
        if (items != null) {
            requestedItems.addAll(items);
            for (DisplayRequestItemDetailsDTO item : items) {
                approvals.put(item.getRequestItemId(), new ItemApproval(item.getRequestedQty()));
            }
        }
        notifyDataSetChanged();
    }

    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
        notifyDataSetChanged();
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    @NonNull
    @Override
    public RequestedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_requested_products, parent, false);
        return new RequestedItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedItemsViewHolder holder, int position) {
        DisplayRequestItemDetailsDTO item = requestedItems.get(position);
        ItemApproval state = approvals.get(item.getRequestItemId());
        if (state == null) {
            state = new ItemApproval(item.getRequestedQty());
            approvals.put(item.getRequestItemId(), state);
        }
        holder.bind(item, state, isReadOnly, () -> {
            if (changeListener != null) changeListener.onApprovalsChanged();
        });
    }

    @Override
    public int getItemCount() {
        return requestedItems.size();
    }

    public int getApprovedCount() {
        int count = 0;
        for (DisplayRequestItemDetailsDTO item : requestedItems) {
            ItemApproval state = approvals.get(item.getRequestItemId());
            if (state == null || (state.approved && state.quantity > 0)) count++;
        }
        return count;
    }

    public boolean allFullyApproved() {
        for (DisplayRequestItemDetailsDTO item : requestedItems) {
            ItemApproval state = approvals.get(item.getRequestItemId());
            if (state != null && (!state.approved || state.quantity != item.getRequestedQty())) return false;
        }
        return true;
    }

    /** Payload for PATCH api/request/{id}/approve (same fields as the website). */
    public List<ApproveRequestDTO> getApprovalList(@Nullable String remarks) {
        List<ApproveRequestDTO> list = new ArrayList<>();
        String safeRemarks = remarks == null || remarks.trim().isEmpty() ? "N/A" : remarks.trim();
        for (DisplayRequestItemDetailsDTO item : requestedItems) {
            ItemApproval state = approvals.get(item.getRequestItemId());
            boolean approved = state == null || (state.approved && state.quantity > 0);
            int qty = state == null ? item.getRequestedQty() : Math.max(0, Math.min(state.quantity, item.getRequestedQty()));

            ApproveRequestDTO dto = new ApproveRequestDTO();
            dto.setProductId(item.getProductId());
            dto.setRequestedItemId(item.getRequestItemId());
            dto.setIsApproved(approved);
            dto.setRequestedItemQty(approved ? qty : 0);
            dto.setRemarks(safeRemarks);
            list.add(dto);
        }
        return list;
    }

    /** Kept for older callers. */
    public List<ApproveRequestDTO> getApprovalList() {
        return getApprovalList(null);
    }
}
