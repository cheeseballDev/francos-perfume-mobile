package com.example.francosperfumemobile.backend.responses.loginresponses;

public class LoginResponse {
    private int EmployeeId;
    private int BranchId;
    private String Role;
    private String Email;
    private String AccessToken;
    private boolean RequiresPasswordChange;
    public int getEmployeeId() {
        return EmployeeId;
    }
    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }
    public int getBranchId() {
        return BranchId;
    }
    public void setBranchId(int branchId) {
        BranchId = branchId;
    }
    public String getRole() {
        return Role;
    }
    public void setRole(String role) {
        Role = role;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getAccessToken() {
        return AccessToken;
    }
    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }
    public boolean isRequiresPasswordChange() {
        return RequiresPasswordChange;
    }
    public void setRequiresPasswordChange(boolean requiresPasswordChange) {
        RequiresPasswordChange = requiresPasswordChange;
    }
}
