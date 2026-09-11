package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.logindtos.LoginDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.OTPDTO;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPIEndpoints {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginDTO dto);

    @POST("api/auth/otp")
    Call<LoginResponse> OTP(@Body OTPDTO dto);
}
