package com.example.francosperfumemobile.backend.responses.loginresponses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("employeeId")
    private int employeeId;
    @SerializedName("branchId")
    private int branchId;
    @SerializedName("role")
    private String role;
    @SerializedName("email")
    private String email;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("requiresPasswordChange")
    private boolean requiresPasswordChange;
    @SerializedName("requiresOtp")
    private boolean requiresOtp;
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public int getBranchId() {
        return branchId;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public boolean isRequiresPasswordChange() {
        return requiresPasswordChange;
    }
    public void setRequiresPasswordChange(boolean requiresPasswordChange) {
        this.requiresPasswordChange = requiresPasswordChange;
    }
    public boolean isRequiresOTP() {
        return requiresOtp;
    }
    public void setRequiresOTP(boolean requiresOTP) {
        this.requiresOtp = requiresOTP;
    }
}
