package com.example.francosperfumemobile.backend.endpoints;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SessionAPIEndpoints {

    @GET("api/session/ping")
    Call<ResponseBody> ping();
}
