package com.example.francosperfumemobile.backend.responses;

import com.example.francosperfumemobile.backend.dtos.NotificationDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationListResponse {
    @SerializedName("notifications") private
    List<NotificationDTO> notifications;
    @SerializedName("unreadCount")
    private int unreadCount;

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
