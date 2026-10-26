package com.example.francosperfumemobile.components;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.adapters.BatchAdapter;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.example.francosperfumemobile.helpers.StatusStyle;

import java.util.Date;

public class BatchViewHolder extends RecyclerView.ViewHolder {

    private static final long DAY_MS = 24L * 60 * 60 * 1000;
    private static final int EXPIRY_WARNING_DAYS = 30;

    TextView cardBatchId, cardBatchQuantity, cardBatchDateReceived, cardBatchTargetDate, expiryBadge;
    View buttonEditBatch;

    public BatchViewHolder(View view) {
        super(view);
        cardBatchId = view.findViewById(R.id.text_view_batch_id);
        cardBatchQuantity = view.findViewById(R.id.text_view_delivery_unit_count);
        cardBatchDateReceived = view.findViewById(R.id.edit_text_batch_date_received);
        cardBatchTargetDate = view.findViewById(R.id.text_view_batch_target_date);
        expiryBadge = view.findViewById(R.id.text_view_batch_expiry_badge);
        buttonEditBatch = view.findViewById(R.id.button_edit_batch);
    }

    public void bind(DisplayBatchDTO item) {
        bind(item, null);
    }

    public void bind(DisplayBatchDTO item, @Nullable BatchAdapter.OnEditBatchListener listener) {
        cardBatchId.setText(item.getBatchDisplayId());
        cardBatchQuantity.setText(String.valueOf(item.getQuantity()));
        cardBatchDateReceived.setText(DateUtils.formatDate(item.getCreatedAt()));
        cardBatchTargetDate.setText(DateUtils.formatDate(item.getExpiryDate()));
        bindExpiryBadge(item.getExpiryDate());

        if (listener != null) {
            buttonEditBatch.setVisibility(View.VISIBLE);
            buttonEditBatch.setOnClickListener(v -> listener.onEditBatchClick(item));
        } else {
            buttonEditBatch.setVisibility(View.GONE);
            buttonEditBatch.setOnClickListener(null);
        }
    }

    private void bindExpiryBadge(String expiryIso) {
        Date expiry = DateUtils.parse(expiryIso);
        if (expiry == null) {
            expiryBadge.setVisibility(View.GONE);
            return;
        }
        long days = (expiry.getTime() - System.currentTimeMillis()) / DAY_MS;
        if (days < 0) {
            expiryBadge.setText(R.string.batch_expired);
            StatusStyle.applyTone(expiryBadge, StatusStyle.Tone.DANGER, R.drawable.ic_alert_triangle);
            expiryBadge.setVisibility(View.VISIBLE);
        } else if (days <= EXPIRY_WARNING_DAYS) {
            expiryBadge.setText(itemView.getResources().getQuantityString(R.plurals.expires_in_days, (int) days, (int) days));
            StatusStyle.applyTone(expiryBadge, StatusStyle.Tone.PENDING, R.drawable.ic_clock);
            expiryBadge.setVisibility(View.VISIBLE);
        } else {
            expiryBadge.setVisibility(View.GONE);
        }
    }
}
