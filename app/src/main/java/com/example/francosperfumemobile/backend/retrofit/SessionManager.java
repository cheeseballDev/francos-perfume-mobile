package com.example.francosperfumemobile.backend.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME = "francos_perfume_session";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EMPLOYEE_ID = "employee_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BRANCH_ID = "branch_id";
    private static final String KEY_ROLE = "role";
    private static final String KEY_REQUIRES_PW_CHANGE = "requires_pw_change";
    private static final String KEY_REQUIRES_OTP = "requires_otp";
    private final SharedPreferences sharedPrefs;

    public SessionManager(Context context){
        sharedPrefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }

    public void createSession(int employeeId, String email, String accessToken, String role, int branchId, boolean passwordChange, boolean otp){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(KEY_EMPLOYEE_ID, employeeId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_ROLE, role);
        editor.putInt(KEY_BRANCH_ID, branchId);
        editor.putBoolean(KEY_REQUIRES_PW_CHANGE, passwordChange);
        editor.putBoolean(KEY_REQUIRES_OTP, otp);
        boolean success = editor.commit();
    }

    public void updateAccessToken(String newToken) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, newToken);
        editor.apply();
    }
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public String getAccessToken() {
        String token = sharedPrefs.getString(KEY_ACCESS_TOKEN, null);
        return token;
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

    public boolean getRequiresPwChange() {
        boolean result = sharedPrefs.getBoolean(KEY_REQUIRES_PW_CHANGE, false);
        return result;
    }

    public boolean isLoggedIn() {
        String token = getAccessToken();
        boolean result = token != null && !token.isEmpty();
        return result;
    }
}
