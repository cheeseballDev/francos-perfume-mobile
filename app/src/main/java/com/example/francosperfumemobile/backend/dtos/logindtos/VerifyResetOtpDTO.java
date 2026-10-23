package com.example.francosperfumemobile.backend.dtos.logindtos;

import com.google.gson.annotations.SerializedName;

public class VerifyResetOtpDTO {
    @SerializedName("email")
    private final String email;
    @SerializedName("otp")
    private final String otp;
    public VerifyResetOtpDTO(String email, String otp) { this.email = email; this.otp = otp; }
}
