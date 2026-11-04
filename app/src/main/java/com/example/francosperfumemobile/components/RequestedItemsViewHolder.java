package com.example.francosperfumemobile.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.RequestedItemsAdapter;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.StatusStyle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RequestedItemsViewHolder extends RecyclerView.ViewHolder {
    TextView productName, productRequestedQty, productId, itemStatus, itemRemarks, approvedLabel;
    CheckBox checkBoxApprove;
    TextInputEditText editTextQty;
    TextInputLayout layoutQty;
    View layoutApproved;

    // The listener currently attached, so it can be removed before the row is reused
    private TextWatcher qtyWatcher;

    public RequestedItemsViewHolder(View view) {
        super(view);
        productName = view.findViewById(R.id.text_view_requested_product_name);
        productId = view.findViewById(R.id.text_view_inventory_product_id);
        productRequestedQty = view.findViewById(R.id.text_view_requested_amount);
        itemStatus = view.findViewById(R.id.text_view_requested_item_status);
        itemRemarks = view.findViewById(R.id.text_view_requested_item_remarks);
        approvedLabel = view.findViewById(R.id.text_view_approved_quantity_label);
        checkBoxApprove = view.findViewById(R.id.check_box_requested_product_approve);
        editTextQty = view.findViewById(R.id.edit_text_requested_products_approved_quantity_amount);
        layoutQty = view.findViewById(R.id.text_input_layout_approved_quantity);
        layoutApproved = view.findViewById(R.id.layout_approved_quantity);
    }

    /** Read-only bind (no approval controls). */
    public void bind(DisplayRequestItemDetailsDTO item) {
        bind(item, null, true, null);
    }

    public void bind(DisplayRequestItemDetailsDTO item, RequestedItemsAdapter.ItemApproval state,
                     boolean readOnly, Runnable onChanged) {
        productName.setText(item.getProductName());
        productId.setText(item.getProductDisplayId());
        productRequestedQty.setText(String.valueOf(item.getRequestedQty()));

        // 1. Detach old listeners first: setChecked/setText below must not write into another item
        checkBoxApprove.setOnCheckedChangeListener(null);
        if (qtyWatcher != null) editTextQty.removeTextChangedListener(qtyWatcher);
        qtyWatcher = null;
        layoutQty.setError(null);

        String status = item.getItemStatus();
        if (status != null && !status.trim().isEmpty() && !"PENDING".equalsIgnoreCase(status)) {
            StatusStyle.apply(itemStatus, status);
            itemStatus.setVisibility(View.VISIBLE);
        } else {
            itemStatus.setVisibility(View.GONE);
        }
        String remarks = item.getRemarks();
        if (remarks != null && !remarks.trim().isEmpty() && !"N/A".equalsIgnoreCase(remarks.trim())) {
            itemRemarks.setText(itemView.getContext().getString(R.string.remarks_format, remarks.trim()));
            itemRemarks.setVisibility(View.VISIBLE);
        } else {
            itemRemarks.setVisibility(View.GONE);
        }

        if (readOnly || state == null) {
            checkBoxApprove.setVisibility(View.GONE);
            if (item.getReceivedQty() > 0) {
                // After delivery: show what actually arrived
                layoutApproved.setVisibility(View.VISIBLE);
                approvedLabel.setText(R.string.received_quantity_text);
                editTextQty.setText(String.valueOf(item.getReceivedQty()));
                editTextQty.setEnabled(false);
            } else {
                layoutApproved.setVisibility(View.GONE);
            }
            return;
        }

        // 2. Editable: show this item's own state
        approvedLabel.setText(R.string.approved_quantity_label);
        layoutApproved.setVisibility(View.VISIBLE);
        checkBoxApprove.setVisibility(View.VISIBLE);
        checkBoxApprove.setChecked(state.approved);
        editTextQty.setEnabled(state.approved);
        editTextQty.setText(String.valueOf(state.quantity));

        // 3. Attach listeners for this item only
        final int max = item.getRequestedQty();
        bindCheckbox(state, max, onChanged);

        qtyWatcher = new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                int qty;
                try {
                    qty = s.length() == 0 ? 0 : Integer.parseInt(s.toString().trim());
                } catch (NumberFormatException e) {
                    qty = 0;
                }
                if (qty > max) {
                    layoutQty.setError(itemView.getContext().getString(R.string.error_max_quantity, max));
                    qty = max;
                } else {
                    layoutQty.setError(null);
                }
                state.quantity = qty;
                boolean approved = qty > 0;
                if (state.approved != approved) {
                    state.approved = approved;
                    checkBoxApprove.setOnCheckedChangeListener(null);
                    checkBoxApprove.setChecked(approved);
                    bindCheckbox(state, max, onChanged);
                }
                if (onChanged != null) onChanged.run();
            }
        };
        editTextQty.addTextChangedListener(qtyWatcher);
    }

    private void bindCheckbox(RequestedItemsAdapter.ItemApproval state, int max, Runnable onChanged) {
        checkBoxApprove.setOnCheckedChangeListener((button, checked) -> {
            state.approved = checked;
            state.quantity = checked ? max : 0;
            editTextQty.setEnabled(checked);
            if (qtyWatcher != null) editTextQty.removeTextChangedListener(qtyWatcher);
            editTextQty.setText(String.valueOf(state.quantity));
            if (qtyWatcher != null) editTextQty.addTextChangedListener(qtyWatcher);
            layoutQty.setError(null);
            if (onChanged != null) onChanged.run();
        });
    }
}
