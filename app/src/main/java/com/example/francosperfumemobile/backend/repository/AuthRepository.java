package com.example.francosperfumemobile.backend.repository;

import com.example.francosperfumemobile.backend.dtos.logindtos.LoginDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.OTPDTO;
import com.example.francosperfumemobile.backend.endpoints.AuthAPIEndpoints;
import com.example.francosperfumemobile.backend.endpoints.InventoryAPIEndpoints;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;
import retrofit2.Call;

public class AuthRepository {
    private final AuthAPIEndpoints authApi;
    public AuthRepository (){
        this.authApi = RetrofitClient.getInstance().create(AuthAPIEndpoints.class);
    }

    public Call<LoginResponse> login(LoginDTO dto) {
        return authApi.login(dto);
    }

    public Call<LoginResponse> OTP(OTPDTO dto) {
        return authApi.OTP(dto);
    }
}
