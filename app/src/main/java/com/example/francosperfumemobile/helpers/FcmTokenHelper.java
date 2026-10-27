package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.francosperfumemobile.backend.repository.AuthRepository;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Sends this phone's Firebase token to the backend (api/auth/register-token) so it can push
 * request/delivery updates. Runs quietly: a failure is logged, never shown to the user.
 */
public final class FcmTokenHelper {

    private static final String TAG = "FCM";

    private FcmTokenHelper() {
    }

    /** After login or OTP: get the current token and register it. */
    public static void registerCurrentToken(Context context) {
        Context appContext = context.getApplicationContext();
        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    registerToken(appContext, task.getResult());
                } else {
                    Log.w(TAG, "Could not get FCM token", task.getException());
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "Firebase not available", e);
        }
    }

    /** From FirebaseMessagingService.onNewToken, or after getToken(). Only when logged in. */
    public static void registerToken(Context context, String token) {
        if (token == null || token.isEmpty()) return;
        Context appContext = context.getApplicationContext();
        if (!new SessionManager(appContext).isLoggedIn()) return;

        new AuthRepository(appContext).registerToken(token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "FCM token registered");
                } else {
                    Log.w(TAG, "FCM token not registered: HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.w(TAG, "FCM token not registered", t);
            }
        });
    }

    /**
     * On logout: delete this phone's token in Firebase. The token the backend still has stops
     * working, so the next person who logs in here won't get the previous user's notifications.
     * A new token is created and registered on the next login.
     */
    public static void deleteToken() {
        try {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) Log.w(TAG, "Could not delete FCM token", task.getException());
            });
        } catch (Exception e) {
            Log.w(TAG, "Firebase not available", e);
        }
    }
}
