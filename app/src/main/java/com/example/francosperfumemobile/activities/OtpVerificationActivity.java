package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.repository.AuthRepository;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResultResponse;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.LoginFlow;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;

/**
 * Second login step for accounts that need a code (website: OtpVerification).
 * Verify → api/auth/OTP, Resend → api/auth/resend-otp (60 s cooldown), Cancel → back to login.
 * Note: right now the backend only sends codes to one test email (no domain yet).
 */
public class OtpVerificationActivity extends AppCompatActivity {

    private static final long RESEND_COOLDOWN_MS = 60_000;
    private static final String STATE_COOLDOWN_END = "cooldown_end";

    private String email;
    private TextInputEditText editTextCode;
    private TextView textViewError;
    private MaterialButton buttonVerify, buttonResend;
    private CountDownTimer resendTimer;
    private long cooldownEndsAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);
        InsetsHelper.applySystemBarsAndIme(findViewById(R.id.otp_verification));

        email = getIntent().getStringExtra(LoginFlow.EXTRA_EMAIL);
        if (email == null || email.isEmpty()) {
            finish();
            return;
        }

        TextView subtitle = findViewById(R.id.text_view_otp_subtitle);
        subtitle.setText(getString(R.string.otp_subtitle, LoginFlow.maskEmail(email)));

        editTextCode = findViewById(R.id.edit_text_otp);
        textViewError = findViewById(R.id.text_view_otp_error);
        buttonVerify = findViewById(R.id.button_verify_otp);
        buttonResend = findViewById(R.id.button_resend_otp);

        findViewById(R.id.button_otp_back).setOnClickListener(v -> cancel());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                cancel();
            }
        });
        findViewById(R.id.button_otp_cancel).setOnClickListener(v -> cancel());
        buttonVerify.setOnClickListener(v -> verify());
        buttonResend.setOnClickListener(v -> resend());

        editTextCode.addTextChangedListener(new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                textViewError.setVisibility(View.GONE);
                if (!ButtonLoading.isLoading(buttonVerify)) {
                    buttonVerify.setEnabled(s.length() == 6);
                }
                if (s.length() == 6) verify(); // verify as soon as all 6 digits are in
            }
        });
        editTextCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                verify();
                return true;
            }
            return false;
        });

        // The login call already sent a code, so start with the cooldown running
        cooldownEndsAt = savedInstanceState != null
                ? savedInstanceState.getLong(STATE_COOLDOWN_END, 0)
                : System.currentTimeMillis() + RESEND_COOLDOWN_MS;
        startCooldown();
        editTextCode.requestFocus();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_COOLDOWN_END, cooldownEndsAt);
    }

    @Override
    protected void onDestroy() {
        if (resendTimer != null) resendTimer.cancel();
        super.onDestroy();
    }

    private void verify() {
        String code = editTextCode.getText() == null ? "" : editTextCode.getText().toString().trim();
        if (code.length() != 6) {
            showError(getString(R.string.error_code_length));
            return;
        }
        if (ButtonLoading.isLoading(buttonVerify)) return;

        ButtonLoading.start(buttonVerify, getString(R.string.verifying));
        new AuthRepository(this).verifyLoginOtp(email, code).enqueue(new SafeCallback<LoginResultResponse<LoginResponse>>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonVerify);
            }

            @Override
            public void onSuccess(@Nullable LoginResultResponse<LoginResponse> body) {
                String error = LoginFlow.finishLogin(OtpVerificationActivity.this, body != null ? body.getData() : null);
                if (error != null) showError(error);
            }

            @Override
            public void onError(@NonNull String message, int code) {
                editTextCode.setText("");
                showError(code == 400 || code == 401 ? getString(R.string.error_code_invalid) : message);
            }
        });
    }

    private void resend() {
        if (System.currentTimeMillis() < cooldownEndsAt || ButtonLoading.isLoading(buttonResend)) return;

        ButtonLoading.start(buttonResend, getString(R.string.sending));
        new AuthRepository(this).resendOtp(email).enqueue(new SafeCallback<ResponseBody>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonResend);
            }

            @Override
            public void onSuccess(@Nullable ResponseBody body) {
                Toast.makeText(OtpVerificationActivity.this, R.string.code_resent, Toast.LENGTH_SHORT).show();
                cooldownEndsAt = System.currentTimeMillis() + RESEND_COOLDOWN_MS;
                startCooldown();
            }

            @Override
            public void onError(@NonNull String message, int code) {
                showError(message);
            }
        });
    }

    private void startCooldown() {
        if (resendTimer != null) resendTimer.cancel();
        long remaining = cooldownEndsAt - System.currentTimeMillis();
        if (remaining <= 0) {
            buttonResend.setEnabled(true);
            buttonResend.setText(R.string.resend_code);
            return;
        }
        buttonResend.setEnabled(false);
        resendTimer = new CountDownTimer(remaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                buttonResend.setText(getString(R.string.resend_in, (int) (millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                buttonResend.setEnabled(true);
                buttonResend.setText(R.string.resend_code);
            }
        }.start();
    }

    private void cancel() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginFlow.EXTRA_EMAIL, email);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
    }
}
