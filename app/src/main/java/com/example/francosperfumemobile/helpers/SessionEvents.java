package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.francosperfumemobile.activities.LoginActivity;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Called by RetrofitClient when the server answers 401 to a logged-in call.
 * Clears the session and opens the login screen, which shows
 * "Your session has expired. Please log in again." under the password field
 * (same behaviour as the website's handleSessionExpired).
 */
public final class SessionEvents {

    public static final String EXTRA_SESSION_EXPIRED = "SESSION_EXPIRED";

    // Several calls can fail at once; only react to the first one
    private static final AtomicBoolean handling = new AtomicBoolean(false);

    private SessionEvents() {
    }

    public static void onSessionExpired(Context context) {
        if (!handling.compareAndSet(false, true)) return;

        Context appContext = context.getApplicationContext();
        new Handler(Looper.getMainLooper()).post(() -> {
            SessionManager sessionManager = new SessionManager(appContext);
            if (!sessionManager.isLoggedIn()) {
                // Already logged out (e.g. the user pressed Logout at the same time)
                handling.set(false);
                return;
            }
            sessionManager.clearSession();

            Intent intent = new Intent(appContext, LoginActivity.class);
            intent.putExtra(EXTRA_SESSION_EXPIRED, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            appContext.startActivity(intent);
        });
    }

    /** Call after a successful login so the next expiry is handled again. */
    public static void reset() {
        handling.set(false);
    }
}
