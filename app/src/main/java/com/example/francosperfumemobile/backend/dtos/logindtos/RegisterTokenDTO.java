package com.example.francosperfumemobile.backend.dtos.logindtos;

import com.google.gson.annotations.SerializedName;

public class RegisterTokenDTO {
    @SerializedName("token")
    private String token;

    public RegisterTokenDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
