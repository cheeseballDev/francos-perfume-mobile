package com.example.francosperfumemobile.backend.endpoints;

public interface NotificationAPIEndpoints {
    @GET("api/notification")
    Call<NotificationListResponse> getNotifications();

    @PATCH("api/notification/{id}/read")
    Call<ResponseBody> markRead(@Path("id") int id);

    @PATCH("api/notification/read-all")
    Call<ResponseBody> markAllRead();
}
