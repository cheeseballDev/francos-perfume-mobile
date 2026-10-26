package com.example.francosperfumemobile.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.FilterManager;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Row of the receive form. Fixes:
 *  - typing writes the RECEIVED amount (item.setReceivedQty), not the requested amount
 *  - the old TextWatcher is removed before the row is reused, so recycled rows don't update the wrong item
 *  - received is kept between 0 and requested
 *  - the reason dropdown is saved (item.setReceiveRemark) and shown when less arrived
 */
public class DeliveryDetailsInboundViewHolder extends RecyclerView.ViewHolder {

    /** Same values as the website's REASON_OPTIONS. Index 0 = no reason. */
    public static final List<String> REASON_VALUES = Arrays.asList("", "DAMAGED", "MISSING", "INCORRECT", "EXPIRED");

    private final TextView productName, productId, requestedQuantity;
    private final CheckBox checkBoxIsReceived;
    private final TextInputEditText editTextReceivedQuantity;
    private final TextInputLayout layoutReceivedQuantity;
    private final View layoutReason;
    private final MaterialAutoCompleteTextView dropdownRemarks;
    private TextWatcher quantityWatcher;

    public interface OnItemChangeListener {
        void onItemChanged(DeliveryItemDTO item);
    }

    public DeliveryDetailsInboundViewHolder(View view) {
        super(view);
        productName = view.findViewById(R.id.text_view_delivery_inbound_product_name);
        productId = view.findViewById(R.id.text_view_delivery_inbound_product_id);
        requestedQuantity = view.findViewById(R.id.text_view_delivered_requested_quantity);
        checkBoxIsReceived = view.findViewById(R.id.check_box_delivery_inbound_received);
        editTextReceivedQuantity = view.findViewById(R.id.edit_text_delivery_inbound_received_quantity);
        layoutReceivedQuantity = view.findViewById(R.id.text_input_layout_received_quantity);
        layoutReason = view.findViewById(R.id.layout_delivery_inbound_reason);
        dropdownRemarks = view.findViewById(R.id.dropdown_delivery_inbound_remarks);
    }

    public void bind(DeliveryItemDTO item, boolean isCompleted, OnItemChangeListener listener) {
        if (item == null) return;
        final int requested = item.getQuantity();

        // Detach listeners from the previous item before filling in this one
        checkBoxIsReceived.setOnCheckedChangeListener(null);
        if (quantityWatcher != null) editTextReceivedQuantity.removeTextChangedListener(quantityWatcher);
        dropdownRemarks.setOnItemClickListener(null);
        layoutReceivedQuantity.setError(null);

        productName.setText(item.getProductName());
        productId.setText(itemView.getContext().getString(R.string.product_number, item.getProductId()));
        requestedQuantity.setText(String.valueOf(requested));

        checkBoxIsReceived.setChecked(item.isReceivedChecked());
        editTextReceivedQuantity.setText(String.valueOf(item.getReceivedQty()));
        checkBoxIsReceived.setEnabled(!isCompleted);
        editTextReceivedQuantity.setEnabled(!isCompleted && item.isReceivedChecked());

        String[] labels = itemView.getResources().getStringArray(R.array.receive_reason_labels);
        dropdownRemarks.setAdapter(new FilterManager.NoFilterAdapter(itemView.getContext(), Arrays.asList(labels)));
        int reasonIndex = Math.max(0, REASON_VALUES.indexOf(item.getReceiveRemark() == null ? "" : item.getReceiveRemark()));
        dropdownRemarks.setText(labels[reasonIndex], false);
        updateReasonVisibility(item, requested);

        if (isCompleted) return;

        checkBoxIsReceived.setOnCheckedChangeListener((button, checked) -> {
            item.setReceivedChecked(checked);
            item.setReceivedQty(checked ? requested : 0);
            if (checked) item.setReceiveRemark(null);
            editTextReceivedQuantity.setEnabled(checked);
            editTextReceivedQuantity.removeTextChangedListener(quantityWatcher);
            editTextReceivedQuantity.setText(String.valueOf(item.getReceivedQty()));
            editTextReceivedQuantity.addTextChangedListener(quantityWatcher);
            layoutReceivedQuantity.setError(null);
            if (checked) dropdownRemarks.setText(labels[0], false);
            updateReasonVisibility(item, requested);
            if (listener != null) listener.onItemChanged(item);
        });

        quantityWatcher = new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                int qty;
                try {
                    qty = s.length() == 0 ? 0 : Integer.parseInt(s.toString().trim());
                } catch (NumberFormatException e) {
                    qty = 0;
                }
                if (qty > requested) {
                    layoutReceivedQuantity.setError(itemView.getContext().getString(R.string.error_max_quantity, requested));
                    qty = requested;
                } else {
                    layoutReceivedQuantity.setError(null);
                }
                item.setReceivedQty(qty);
                updateReasonVisibility(item, requested);
                if (listener != null) listener.onItemChanged(item);
            }
        };
        editTextReceivedQuantity.addTextChangedListener(quantityWatcher);

        dropdownRemarks.setOnItemClickListener((parent, view, position, id) -> {
            item.setReceiveRemark(position == 0 ? null : REASON_VALUES.get(position));
            if (listener != null) listener.onItemChanged(item);
        });
    }

    private void updateReasonVisibility(DeliveryItemDTO item, int requested) {
        boolean shortfall = !item.isReceivedChecked() || item.getReceivedQty() < requested;
        layoutReason.setVisibility(shortfall ? View.VISIBLE : View.GONE);
    }
}
