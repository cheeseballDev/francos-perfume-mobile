package com.example.francosperfumemobile.backend.endpoints;

import com.example.francosperfumemobile.backend.dtos.logindtos.EmailDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.LoginDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.OTPDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.RegisterTokenDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.ResetPasswordDTO;
import com.example.francosperfumemobile.backend.dtos.logindtos.VerifyResetOtpDTO;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResultResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.VerifyResetOtpResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPIEndpoints {

    @POST("api/auth/login")
    Call<LoginResultResponse<LoginResponse>> login(@Body LoginDTO request);

    @POST("api/auth/otp")
    Call<LoginResultResponse<LoginResponse>> OTP(@Body OTPDTO dto);

    @POST("api/auth/register-token")
    Call<Void> registerToken(@Body RegisterTokenDTO dto);

    @POST("api/auth/send-forgot-password-otp")
    Call<ResponseBody> sendForgotPasswordOTP(@Body EmailDTO dto);

    @POST("api/auth/verify-forgot-password-otp")
    Call<VerifyResetOtpResponse> verifyForgotPasswordOtp(@Body VerifyResetOtpDTO dto);

    @POST("api/auth/reset")
    Call<ResponseBody> resetPassword(@Body ResetPasswordDTO dto);
}
