package com.example.francosperfumemobile.backend.dtos.logindtos;

public class LoginDTO {
    private String Email;
    private String Password;
    private String NewPassword;

    public String getEmail() {
        return Email;
    }
    public String getPassword() {
        return Password;
    }
    public String getNewPassword() {
        return NewPassword;
    }
    public void setEmail(String email){
        this.Email = email;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }
}
