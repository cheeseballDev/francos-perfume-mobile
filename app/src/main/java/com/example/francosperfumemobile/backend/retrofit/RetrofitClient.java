package com.example.francosperfumemobile.backend.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    // TODO: Remove upon production
    private static final String BASE_URL = "http://192.168.254.195:5000/";
    public static Retrofit getInstance(Context context) {
        SessionManager sm = new SessionManager(context);
        if(retrofit == null){
            Interceptor authInterceptor = chain -> {
                Request originalRequest = chain.request();
                String token = sm.getAccessToken();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                if(token != null && !token.isEmpty()){
                    requestBuilder.header("Authorization", "Bearer " + token);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            };

            OkHttpClient okHttpClient =  new OkHttpClient.Builder()
                        .addInterceptor(authInterceptor)
                        .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}