package com.example.francosperfumemobile.backend.responses.loginresponses;

import com.google.gson.annotations.SerializedName;

public class VerifyResetOtpResponse {
    @SerializedName("token")
    private String token;
    public String getToken() { return token; }
}
