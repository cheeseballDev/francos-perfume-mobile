package com.example.francosperfumemobile.backend.responses.loginresponses;

import com.google.gson.annotations.SerializedName;

/**
 * Reply of api/auth/verify-forgot-password-otp.
 * The backend sends {success, message, data: {token, firstName, lastName}} (the website reads
 * response.data.data). Older builds sent the token at the top level, so both shapes work:
 * call resolve() and read the fields from what it returns.
 */
public class VerifyResetOtpResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private VerifyResetOtpResponse data;

    /** The object that actually holds the token (data if present, otherwise this). */
    public VerifyResetOtpResponse resolve() {
        return data != null ? data : this;
    }

    public String getToken() { return token; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getMessage() { return message; }
}
