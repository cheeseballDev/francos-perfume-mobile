package com.example.francosperfumemobile.backend.dtos;

import com.google.gson.annotations.SerializedName;

public class NotificationDTO {
    @SerializedName("notificationId")
    private int notificationId;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("type")
    private String type;
    @SerializedName("referenceId")
    private String referenceId;
    @SerializedName("isRead")
    private boolean isRead;
    @SerializedName("createdAt")
    private String createdAt;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
