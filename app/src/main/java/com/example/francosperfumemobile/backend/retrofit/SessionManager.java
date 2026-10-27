package com.example.francosperfumemobile.backend.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;

/**
 * The logged-in user, stored in private SharedPreferences.
 * Only saved after a real token comes back (normal login or OTP). The password is never stored.
 * This file is excluded from backups (res/xml/backup_rules.xml, data_extraction_rules.xml).
 */
public class SessionManager {
    public static final String PREFS_NAME = "francos_perfume_session";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EMPLOYEE_ID = "employee_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_BRANCH_ID = "branch_id";
    private static final String KEY_BRANCH_LOCATION = "branch_location";
    private static final String KEY_ROLE = "role";
    private static final String KEY_REQUIRES_PW_CHANGE = "requires_pw_change";
    private static final String KEY_REQUIRES_OTP = "requires_otp";

    private final SharedPreferences sharedPrefs;

    public SessionManager(Context context) {
        sharedPrefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void createSession(LoginResponse data) {
        sharedPrefs.edit()
                .putInt(KEY_EMPLOYEE_ID, data.getEmployeeId())
                .putString(KEY_EMAIL, data.getEmail())
                .putString(KEY_FIRST_NAME, data.getFirstName())
                .putString(KEY_LAST_NAME, data.getLastName())
                .putString(KEY_ACCESS_TOKEN, data.getAccessToken())
                .putString(KEY_ROLE, data.getRole())
                .putInt(KEY_BRANCH_ID, data.getBranchId())
                .putString(KEY_BRANCH_LOCATION, data.getBranchLocation())
                .putBoolean(KEY_REQUIRES_PW_CHANGE, false)
                .putBoolean(KEY_REQUIRES_OTP, false)
                .commit();
    }

    /** Kept for older callers. Prefer createSession(LoginResponse). */
    public void createSession(int employeeId, String email, String accessToken, String role, int branchId, String branchLocation, boolean passwordChange, boolean otp) {
        sharedPrefs.edit()
                .putInt(KEY_EMPLOYEE_ID, employeeId)
                .putString(KEY_EMAIL, email)
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_ROLE, role)
                .putInt(KEY_BRANCH_ID, branchId)
                .putString(KEY_BRANCH_LOCATION, branchLocation)
                .putBoolean(KEY_REQUIRES_PW_CHANGE, passwordChange)
                .putBoolean(KEY_REQUIRES_OTP, otp)
                .commit();
    }

    public void updateAccessToken(String newToken) {
        sharedPrefs.edit().putString(KEY_ACCESS_TOKEN, newToken).apply();
    }

    public void clearSession() {
        sharedPrefs.edit().clear().commit();
    }

    public String getAccessToken() {
        return sharedPrefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public int getEmployeeId() {
        return sharedPrefs.getInt(KEY_EMPLOYEE_ID, -1);
    }

    public int getBranchId() {
        return sharedPrefs.getInt(KEY_BRANCH_ID, -1);
    }

    public String getRole() {
        return sharedPrefs.getString(KEY_ROLE, null);
    }

    public String getEmail() {
        return sharedPrefs.getString(KEY_EMAIL, null);
    }

    public String getFirstName() {
        return sharedPrefs.getString(KEY_FIRST_NAME, null);
    }

    public String getLastName() {
        return sharedPrefs.getString(KEY_LAST_NAME, null);
    }

    public String getFullName() {
        String first = getFirstName() != null ? getFirstName().trim() : "";
        String last = getLastName() != null ? getLastName().trim() : "";
        String full = (first + " " + last).trim();
        return full.isEmpty() ? getEmail() : full;
    }

    public boolean getRequiresPwChange() {
        return sharedPrefs.getBoolean(KEY_REQUIRES_PW_CHANGE, false);
    }

    public boolean isLoggedIn() {
        String token = getAccessToken();
        return token != null && !token.isEmpty();
    }

    public String getBranchLocation() {
        return sharedPrefs.getString(KEY_BRANCH_LOCATION, null);
    }
}
