package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.responses.NotificationListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface NotificationAPIEndpoints {
    @GET("api/notification")
    Call<NotificationListResponse> getNotifications();

    @PATCH("api/notification/{id}/read")
    Call<ResponseBody> markRead(@Path("id") int id);

    @PATCH("api/notification/read-all")
    Call<ResponseBody> markAllRead();
}
