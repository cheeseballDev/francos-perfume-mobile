package com.example.francosperfumemobile.components;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.deliverydtos.DeliveryItemDTO;

public class DeliveryDetailsInboundViewHolder extends RecyclerView.ViewHolder {
    //todo: add listeners in this

    private ImageView deliveryItemsImage;
    private TextView deliveryItemsProductName, deliveryItemsProductId, deliveryItemsRequestedQuantity;
    private CheckBox checkBoxIsReceived;
    private EditText editTextReceivedQuantity, editTextAdditionalRemarks;
    private Spinner dropdownRemarks;

    public interface OnItemChangeListener {
        void onItemChanged(DeliveryItemDTO item);
    }

    public DeliveryDetailsInboundViewHolder(View view) {
        super(view);
        deliveryItemsProductName = view.findViewById(R.id.text_view_delivery_inbound_product_name);
        deliveryItemsProductId = view.findViewById(R.id.text_view_delivery_inbound_product_id);
        deliveryItemsRequestedQuantity = view.findViewById(R.id.text_view_delivered_requested_quantity);
        checkBoxIsReceived = view.findViewById(R.id.check_box_delivery_inbound_received);
        editTextReceivedQuantity = view.findViewById(R.id.edit_text_delivery_inbound_received_quantity);
        editTextAdditionalRemarks = view.findViewById(R.id.edit_text_delivery_inbound_additional_remarks);
        dropdownRemarks = view.findViewById(R.id.dropdown_delivery_inbound_remarks);
    }

    public void bind(DeliveryItemDTO item, OnItemChangeListener listener) {
        if (item == null) return;

        deliveryItemsProductName.setText(item.getProductName());
        deliveryItemsProductId.setText(item.getProductId());
        //todo: maybe getQuantity is different from requested quantity
        deliveryItemsRequestedQuantity.setText(item.getQuantity());
        deliveryItemsRequestedQuantity.setText(0);
        // todo: if checkbox has one missing then mark the delivery as "partial"
        // todo: if editTextReceivedQuantity is different from requestedquantity then mark the delivery as "partial"
        checkBoxIsReceived.setOnCheckedChangeListener(null);


        checkBoxIsReceived.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                item.setQuantity(0);
                editTextReceivedQuantity.setText(0);
            } else {
                item.setQuantity(Integer.parseInt(editTextReceivedQuantity.getText().toString()));
                listener.onItemChanged(item);
            }


        });

        // is this automatic in the backend?
        // TODO: Determine if ONE checkbox is left uncheck
        // TODO: Determine if ONE EditText is DIFFERENT from the requested quantity
        // TODO: after that, mark the delivery as "partial"

        editTextReceivedQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int qty = Integer.parseInt(s.toString().trim());
                    item.setQuantity(qty);
                } catch (NumberFormatException e) {
                    item.setQuantity(0);
                }
                if (listener != null) listener.onItemChanged(item);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }
}
