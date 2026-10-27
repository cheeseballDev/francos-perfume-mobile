package com.example.francosperfumemobile.backend.retrofit;

import android.content.Context;

import com.example.francosperfumemobile.BuildConfig;
import com.example.francosperfumemobile.helpers.SessionEvents;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * One Retrofit instance for the whole app.
 *
 * - Uses the application context, so it never holds on to an Activity.
 * - Adds "Authorization: Bearer <token>" when the user is logged in.
 * - A 401 on a normal (non-auth) call with a token means the session expired:
 *   SessionEvents logs the user out and opens the login screen with a message.
 * - The server address comes from BuildConfig.BASE_URL (app/build.gradle.kts).
 */
public class RetrofitClient {

    private static volatile Retrofit retrofit;

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = build(context.getApplicationContext());
                }
            }
        }
        return retrofit;
    }

    private static Retrofit build(Context appContext) {
        SessionManager sessionManager = new SessionManager(appContext);

        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            String token = sessionManager.getAccessToken();
            Request.Builder builder = original.newBuilder();
            boolean sentToken = token != null && !token.isEmpty();
            if (sentToken) {
                builder.header("Authorization", "Bearer " + token);
            }

            Response response = chain.proceed(builder.build());

            if (response.code() == 401 && sentToken && !isAuthCall(original)) {
                SessionEvents.onSessionExpired(appContext);
            }
            return response;
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Login, OTP and password calls return 401 for wrong passwords/codes; that is not an expired session
    private static boolean isAuthCall(Request request) {
        String path = request.url().encodedPath().toLowerCase(java.util.Locale.ROOT);
        return path.contains("/api/auth/") && !path.endsWith("/register-token");
    }
}
