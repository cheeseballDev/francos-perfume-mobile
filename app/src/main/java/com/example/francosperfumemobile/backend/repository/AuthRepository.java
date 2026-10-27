package com.example.francosperfumemobile.backend.repository;

import android.content.Context;

import com.example.francosperfumemobile.backend.dtos.logindtos.EmailDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.LoginDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.OTPDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.RegisterTokenDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.ResetPasswordDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.VerifyResetOtpDTO;
import com.example.francosperfumemobile.backend.endpoints.AuthAPIEndpoints;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResultResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.VerifyResetOtpResponse;
import com.example.francosperfumemobile.backend.retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthRepository {

    private final AuthAPIEndpoints authApi;

    public AuthRepository(Context context) {
        this.authApi = RetrofitClient.getInstance(context).create(AuthAPIEndpoints.class);
    }

    public Call<LoginResultResponse<LoginResponse>> login(LoginDTO dto) {
        return authApi.login(dto);
    }

    public Call<LoginResultResponse<LoginResponse>> login(String email, String password) {
        LoginDTO dto = new LoginDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        return authApi.login(dto);
    }

    /** First login with a temporary password: same endpoint, with NewPassword filled in (website: handlePasswordUpdate). */
    public Call<LoginResultResponse<LoginResponse>> changeTemporaryPassword(String email, String temporaryPassword, String newPassword) {
        LoginDTO dto = new LoginDTO();
        dto.setEmail(email);
        dto.setPassword(temporaryPassword);
        dto.setNewPassword(newPassword);
        return authApi.login(dto);
    }

    public Call<LoginResultResponse<LoginResponse>> OTP(OTPDTO dto) {
        return authApi.OTP(dto);
    }

    public Call<LoginResultResponse<LoginResponse>> verifyLoginOtp(String email, String code) {
        OTPDTO dto = new OTPDTO();
        dto.setEmail(email);
        dto.setOTP(code);
        return authApi.OTP(dto);
    }

    public Call<ResponseBody> resendOtp(String email) {
        return authApi.resendOtp(new EmailDTO(email));
    }

    public Call<Void> registerToken(String token) {
        return authApi.registerToken(new RegisterTokenDTO(token));
    }

    // ------------------------------------------------------------ forgot password

    public Call<ResponseBody> sendForgotPasswordOtp(String email) {
        return authApi.sendForgotPasswordOTP(new EmailDTO(email));
    }

    public Call<VerifyResetOtpResponse> verifyForgotPasswordOtp(String email, String code) {
        return authApi.verifyForgotPasswordOtp(new VerifyResetOtpDTO(email, code));
    }

    public Call<ResponseBody> resetPassword(String email, String resetToken, String newPassword) {
        return authApi.resetPassword(new ResetPasswordDTO(email, resetToken, newPassword));
    }
}
