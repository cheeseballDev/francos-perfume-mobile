package com.example.francosperfumemobile.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.requestdtos.ApproveRequestDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.DisplayRequestItemDetailsDTO;
import com.example.francosperfumemobile.components.RequestedItemsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestedItemsAdapter extends RecyclerView.Adapter<RequestedItemsViewHolder> {

    private final List<DisplayRequestItemDetailsDTO> requestedItems;
    private final Map<Integer, Integer> modifiedQuantities = new HashMap<>();
    private final Map<Integer, Boolean> approvalStatusMap = new HashMap<>();
    private boolean isReadOnly = false;

    public RequestedItemsAdapter(List<DisplayRequestItemDetailsDTO> requestedItems) {
        this.requestedItems = requestedItems;
    }

    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
        notifyDataSetChanged();
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
        holder.bind(item);

        TextView textRequestedQty = holder.itemView.findViewById(R.id.text_view_requested_amount);
        TextView textProductName = holder.itemView.findViewById(R.id.text_view_requested_product_name);
        TextView textProductId = holder.itemView.findViewById(R.id.text_view_inventory_product_id);
        CheckBox checkBoxApprove = holder.itemView.findViewById(R.id.check_box_requested_product_approve);
        EditText editTextQty = holder.itemView.findViewById(R.id.edit_text_requested_products_approved_quantity_amount);

        if (textProductName != null) textProductName.setText(item.getProductName());
        if (textProductId != null) textProductId.setText(item.getProductDisplayId());
        if (textRequestedQty != null) textRequestedQty.setText(String.valueOf(item.getRequestedQty()));

        boolean isApproved = approvalStatusMap.containsKey(item.getRequestItemId())
                ? approvalStatusMap.get(item.getRequestItemId())
                : true;

        if (checkBoxApprove != null) {
            checkBoxApprove.setChecked(isReadOnly ? true : isApproved);
            checkBoxApprove.setEnabled(!isReadOnly);
            checkBoxApprove.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isReadOnly) {
                    approvalStatusMap.put(item.getRequestItemId(), isChecked);
                }
            });
        }

        if (editTextQty != null) {
            if (editTextQty.getTag() instanceof TextWatcher) {
                editTextQty.removeTextChangedListener((TextWatcher) editTextQty.getTag());
            }

            int currentQty = modifiedQuantities.containsKey(item.getRequestItemId())
                    ? modifiedQuantities.get(item.getRequestItemId())
                    : item.getRequestedQty();

            editTextQty.setText(String.valueOf(currentQty));
            editTextQty.setEnabled(!isReadOnly);
            editTextQty.setFocusable(!isReadOnly);
            editTextQty.setFocusableInTouchMode(!isReadOnly);

            if (!isReadOnly) {
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            int qty = s.length() > 0 ? Integer.parseInt(s.toString()) : 0;
                            modifiedQuantities.put(item.getRequestItemId(), qty);
                        } catch (NumberFormatException e) {
                            modifiedQuantities.put(item.getRequestItemId(), 0);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                };

                editTextQty.addTextChangedListener(textWatcher);
                editTextQty.setTag(textWatcher);
            }
        }
    }

    @Override
    public int getItemCount() {
        return requestedItems != null ? requestedItems.size() : 0;
    }

    public List<ApproveRequestDTO> getApprovalList() {
        List<ApproveRequestDTO> approvals = new ArrayList<>();
        if (requestedItems != null) {
            for (DisplayRequestItemDetailsDTO item : requestedItems) {
                ApproveRequestDTO dto = new ApproveRequestDTO();

                dto.setProductId(item.getProductId());
                dto.setRequestedItemId(item.getRequestItemId());

                boolean isApproved = approvalStatusMap.containsKey(item.getRequestItemId())
                        ? approvalStatusMap.get(item.getRequestItemId())
                        : true;
                dto.setIsApproved(isApproved);

                int qtyToApprove = modifiedQuantities.containsKey(item.getRequestItemId())
                        ? modifiedQuantities.get(item.getRequestItemId())
                        : item.getRequestedQty();

                dto.setRequestedItemQty(qtyToApprove);
                dto.setRemarks(null);

                approvals.add(dto);
            }
        }
        return approvals;
    }
}