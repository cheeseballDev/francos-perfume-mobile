package com.example.francosperfumemobile.backend.responses;

import com.example.francosperfumemobile.backend.dtos.NotificationDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class NotificationListResponse {
    @SerializedName("notifications")
    private List<NotificationDTO> notifications;
    @SerializedName("unreadCount")
    private int unreadCount;

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    /** Reads either {notifications, unreadCount} or a bare list of notifications. */
    public static NotificationListResponse from(JsonElement json) {
        NotificationListResponse result = new NotificationListResponse();
        result.notifications = new ArrayList<>();
        if (json == null || json.isJsonNull()) return result;

        Gson gson = new Gson();
        if (json.isJsonArray()) {
            List<NotificationDTO> list = gson.fromJson(json, new TypeToken<List<NotificationDTO>>() {}.getType());
            if (list != null) result.notifications = list;
            for (NotificationDTO n : result.notifications) {
                if (!n.isRead()) result.unreadCount++;
            }
            return result;
        }
        if (json.isJsonObject() && json.getAsJsonObject().has("data") && !json.getAsJsonObject().has("notifications")) {
            return from(json.getAsJsonObject().get("data"));
        }
        NotificationListResponse parsed = gson.fromJson(json, NotificationListResponse.class);
        if (parsed != null) {
            if (parsed.notifications == null) parsed.notifications = new ArrayList<>();
            return parsed;
        }
        return result;
    }
}
