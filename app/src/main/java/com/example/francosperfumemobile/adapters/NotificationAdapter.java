package com.example.francosperfumemobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.NotificationDTO;
import com.example.francosperfumemobile.components.NotificationViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    public interface OnNotificationClick {
        void onNotificationClick(NotificationDTO notification, int position);
    }

    private final List<NotificationDTO> notifications = new ArrayList<>();
    private final OnNotificationClick listener;

    public NotificationAdapter(OnNotificationClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationDTO item = notifications.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) listener.onNotificationClick(notifications.get(pos), pos);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setItems(List<NotificationDTO> items) {
        notifications.clear();
        if (items != null) notifications.addAll(items);
        notifyDataSetChanged();
    }

    public void markRead(int position) {
        if (position < 0 || position >= notifications.size()) return;
        notifications.get(position).setRead(true);
        notifyItemChanged(position);
    }

    public void markAllRead() {
        for (NotificationDTO n : notifications) n.setRead(true);
        notifyDataSetChanged();
    }

    public int unreadCount() {
        int count = 0;
        for (NotificationDTO n : notifications) if (!n.isRead()) count++;
        return count;
    }
}
