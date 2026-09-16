package com.example.francosperfumemobile.backend.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME = "francos_perfume_session";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EMPLOYEE_ID = "employee_id";
    private static final String KEY_BRANCH_ID = "branch_id";
    private static final String KEY_ROLE = "role";
    private static final String KEY_REQUIRES_PW_CHANGE = "requires_pw_change";
    private final SharedPreferences sharedPrefs;

    public SessionManager(Context context){
        sharedPrefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }

    public void createSession(String accessToken, int employeeId, int branchId, String role, boolean passwordChange){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putInt(KEY_EMPLOYEE_ID, employeeId);
        editor.putInt(KEY_BRANCH_ID, branchId);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_REQUIRES_PW_CHANGE, passwordChange);
        editor.apply();
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

    public boolean getRequiresPwChange() {
        return sharedPrefs.getBoolean(KEY_REQUIRES_PW_CHANGE, false);
    }

    public boolean isLoggedIn() {
        return getAccessToken() != null && !getAccessToken().isEmpty();
    }
}
