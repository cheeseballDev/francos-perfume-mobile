package com.example.francosperfumemobile.helpers;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.activities.ChangeTemporaryPasswordActivity;
import com.example.francosperfumemobile.activities.MainActivity;
import com.example.francosperfumemobile.activities.OtpVerificationActivity;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;

/**
 * What happens after the login endpoint answers (website: useLogin continueLogin /
 * processSuccessfulLogin). Shared by the login, temporary-password and OTP screens.
 *
 *   requiresPasswordChange → ChangeTemporaryPasswordActivity
 *   requiresOtp            → OtpVerificationActivity
 *   otherwise              → save the session, register for push, open MainActivity
 *
 * Nothing is saved until a real access token comes back.
 */
public final class LoginFlow {

    public static final String EXTRA_EMAIL = "LOGIN_EMAIL";
    public static final String EXTRA_PASSWORD = "LOGIN_PASSWORD";
    public static final String EXTRA_FIRST_NAME = "LOGIN_FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "LOGIN_LAST_NAME";

    private LoginFlow() {
    }

    /**
     * @return an error message to show, or null if the user was sent to the next screen
     */
    @Nullable
    public static String continueLogin(Activity activity, @Nullable LoginResponse data, String email, String password) {
        if (data == null) return activity.getString(R.string.error_login_generic);

        // Block roles without mobile screens early, when the backend tells us the role
        if (data.getRole() != null && !AccessRules.canUseMobileApp(data.getRole())) {
            return activity.getString(R.string.error_role_not_allowed, StatusStyle.label(data.getRole()));
        }

        if (data.isRequiresPasswordChange()) {
            Intent intent = new Intent(activity, ChangeTemporaryPasswordActivity.class);
            intent.putExtra(EXTRA_EMAIL, email);
            intent.putExtra(EXTRA_PASSWORD, password);
            intent.putExtra(EXTRA_FIRST_NAME, data.getFirstName());
            intent.putExtra(EXTRA_LAST_NAME, data.getLastName());
            activity.startActivity(intent);
            return null;
        }

        if (data.isRequiresOTP()) {
            Intent intent = new Intent(activity, OtpVerificationActivity.class);
            intent.putExtra(EXTRA_EMAIL, email);
            activity.startActivity(intent);
            return null;
        }

        return finishLogin(activity, data);
    }

    /**
     * Saves the session and opens the app.
     * @return an error message, or null when MainActivity was opened
     */
    @Nullable
    public static String finishLogin(Activity activity, @Nullable LoginResponse data) {
        if (data == null || data.getAccessToken() == null || data.getAccessToken().isEmpty()) {
            return activity.getString(R.string.error_login_generic);
        }
        if (!AccessRules.canUseMobileApp(data.getRole())) {
            return activity.getString(R.string.error_role_not_allowed, StatusStyle.label(data.getRole()));
        }

        new SessionManager(activity).createSession(data);
        SessionEvents.reset();
        FcmTokenHelper.registerCurrentToken(activity);

        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
        return null;
    }

    /** "kevin@gmail.com" → "k•••@gmail.com" for "we sent a code to ..." */
    public static String maskEmail(@Nullable String email) {
        if (email == null) return "";
        int at = email.indexOf('@');
        if (at <= 1) return email;
        return email.charAt(0) + "•••" + email.substring(at);
    }
}
