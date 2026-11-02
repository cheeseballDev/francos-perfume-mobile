package com.example.francosperfumemobile.backend.repository;

import android.content.Context;

import com.example.francosperfumemobile.backend.endpoints.SessionAPIEndpoints;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SessionRepository {

    private final SessionAPIEndpoints api;

    public SessionRepository(Context context) {
        this.api = RetrofitClient.getInstance(context).create(SessionAPIEndpoints.class);
    }

    /** A 401 here is handled by RetrofitClient, which logs the user out. */
    public Call<ResponseBody> ping() {
        return api.ping();
    }
}
