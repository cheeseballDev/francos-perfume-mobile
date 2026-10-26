package com.example.francosperfumemobile.backend.endpoints;

import com.google.gson.JsonElement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface NotificationAPIEndpoints {

    // Read as JSON so both {notifications, unreadCount} and a plain list work (like the website)
    @GET("api/notification")
    Call<JsonElement> getNotifications();

    @PATCH("api/notification/{id}/read")
    Call<ResponseBody> markRead(@Path("id") int id);

    @PATCH("api/notification/read-all")
    Call<ResponseBody> markAllRead();
}
