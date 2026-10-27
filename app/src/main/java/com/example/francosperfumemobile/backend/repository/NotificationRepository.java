package com.example.francosperfumemobile.backend.repository;

import android.content.Context;

import com.example.francosperfumemobile.backend.endpoints.NotificationAPIEndpoints;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;
import com.google.gson.JsonElement;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepository {

    private final NotificationAPIEndpoints api;

    public NotificationRepository(Context context) {
        this.api = RetrofitClient.getInstance(context).create(NotificationAPIEndpoints.class);
    }

    /** Parse the result with NotificationListResponse.from(json). */
    public Call<JsonElement> getNotifications() {
        return api.getNotifications();
    }

    public Call<ResponseBody> markRead(int notificationId) {
        return api.markRead(notificationId);
    }

    public Call<ResponseBody> markAllRead() {
        return api.markAllRead();
    }
}
