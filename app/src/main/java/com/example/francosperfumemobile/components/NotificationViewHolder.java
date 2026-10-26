package com.example.francosperfumemobile.components;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.NotificationDTO;
import com.example.francosperfumemobile.helpers.DateUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    private final TextView title, message, time;
    private final View unreadDot, iconTile;
    private final ImageView icon;
    private final MaterialCardView card;

    public NotificationViewHolder(View view) {
        super(view);
        card = view.findViewById(R.id.card_notification);
        title = view.findViewById(R.id.text_view_notification_title);
        message = view.findViewById(R.id.text_view_notification_message);
        time = view.findViewById(R.id.text_view_notification_time);
        unreadDot = view.findViewById(R.id.view_unread_dot);
        icon = view.findViewById(R.id.image_view_notification_icon);
        iconTile = view.findViewById(R.id.layout_notification_icon);
    }

    public void bind(NotificationDTO item) {
        title.setText(item.getTitle() != null && !item.getTitle().isEmpty() ? item.getTitle() : itemView.getContext().getString(R.string.notification_default_title));
        message.setText(item.getMessage());
        time.setText(DateUtils.relative(item.getCreatedAt()));

        boolean unread = !item.isRead();
        unreadDot.setVisibility(unread ? View.VISIBLE : View.GONE);
        title.setTypeface(title.getTypeface(), unread ? Typeface.BOLD : Typeface.NORMAL);
        card.setStrokeColor(ContextCompat.getColor(itemView.getContext(), unread ? R.color.fp_primary_pressed : R.color.fp_border));
        card.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), unread ? R.color.fp_accent : R.color.fp_card));

        String type = item.getType() == null ? "" : item.getType().toUpperCase(Locale.ROOT);
        int iconRes = R.drawable.ic_bell;
        int colorRes = R.color.fp_muted_foreground;
        if (type.contains("DELIVERY")) {
            iconRes = R.drawable.ic_truck;
            colorRes = R.color.icon_blue;
        } else if (type.contains("REQUEST")) {
            iconRes = R.drawable.ic_file_text;
            colorRes = R.color.icon_amber;
        } else if (type.contains("STOCK") || type.contains("INVENTORY")) {
            iconRes = R.drawable.ic_alert_triangle;
            colorRes = R.color.icon_red;
        }
        int color = ContextCompat.getColor(itemView.getContext(), colorRes);
        icon.setImageResource(iconRes);
        icon.setImageTintList(ColorStateList.valueOf(color));
        iconTile.setBackgroundTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 36)));

        itemView.setContentDescription((unread ? "Unread. " : "") + title.getText() + ". " + message.getText() + ". " + time.getText());
    }
}
