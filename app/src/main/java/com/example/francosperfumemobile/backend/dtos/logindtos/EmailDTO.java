package com.example.francosperfumemobile.backend.dtos.logindtos;

import com.google.gson.annotations.SerializedName;

public class EmailDTO {
    @SerializedName("email")
    private final String email;
    public EmailDTO(String email) { this.email = email; }
}
