package com.example.francosperfumemobile.backend.dtos.logindtos;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordDTO {
    @SerializedName("email")
    private final String email;
    @SerializedName("resetToken")
    private final String resetToken;
    @SerializedName("newPassword")
    private final String newPassword;
    public ResetPasswordDTO(String email, String resetToken, String newPassword) {
        this.email = email; this.resetToken = resetToken; this.newPassword = newPassword;
    }
}
