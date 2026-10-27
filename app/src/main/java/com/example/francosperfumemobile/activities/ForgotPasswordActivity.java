package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.repository.AuthRepository;
import com.example.francosperfumemobile.backend.responses.loginresponses.VerifyResetOtpResponse;
import com.example.francosperfumemobile.components.PasswordConditionsView;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.LoginFlow;
import com.example.francosperfumemobile.helpers.PasswordRules;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;

/**
 * Forgot password in three steps, same as the website's ForgotPasswordPage:
 *  1. email          → api/auth/send-forgot-password-otp
 *  2. 6-digit code   → api/auth/verify-forgot-password-otp (returns token + first/last name)
 *  3. new password   → api/auth/reset  (live checklist incl. "must not include your name")
 * Note: right now the backend only emails codes to one test address (no domain yet).
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private static final int STEP_EMAIL = 0, STEP_CODE = 1, STEP_PASSWORD = 2;
    private static final long RESEND_COOLDOWN_MS = 60_000;

    private static final String STATE_STEP = "step";
    private static final String STATE_EMAIL = "email";
    private static final String STATE_TOKEN = "token";
    private static final String STATE_FIRST = "first";
    private static final String STATE_LAST = "last";
    private static final String STATE_COOLDOWN = "cooldown";

    private int step = STEP_EMAIL;
    private String email = "";
    private String resetToken;
    private String firstName = "", lastName = "";
    private long cooldownEndsAt = 0;
    private CountDownTimer resendTimer;

    private ViewFlipper flipper;
    private TextView textViewStep, textViewSubtitle;
    private ImageView imageViewIcon;
    private LinearProgressIndicator progressSteps;

    private TextInputLayout layoutEmail, layoutConfirm;
    private TextInputEditText editTextEmail, editTextCode, editTextNewPassword, editTextConfirm;
    private TextView errorEmail, errorCode, errorPassword;
    private MaterialButton buttonSendCode, buttonVerify, buttonResend, buttonReset;
    private PasswordConditionsView conditionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        InsetsHelper.applySystemBarsAndIme(findViewById(R.id.forgot_password));

        bindViews();
        setListeners();

        if (savedInstanceState != null) {
            step = savedInstanceState.getInt(STATE_STEP, STEP_EMAIL);
            email = savedInstanceState.getString(STATE_EMAIL, "");
            resetToken = savedInstanceState.getString(STATE_TOKEN);
            firstName = savedInstanceState.getString(STATE_FIRST, "");
            lastName = savedInstanceState.getString(STATE_LAST, "");
            cooldownEndsAt = savedInstanceState.getLong(STATE_COOLDOWN, 0);
            if (step == STEP_PASSWORD && resetToken == null) step = STEP_EMAIL;
        } else {
            String prefill = getIntent().getStringExtra(LoginFlow.EXTRA_EMAIL);
            if (prefill != null) editTextEmail.setText(prefill);
        }
        showStep(step);
        if (step == STEP_CODE) startCooldown();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_STEP, step);
        outState.putString(STATE_EMAIL, email);
        outState.putString(STATE_TOKEN, resetToken);
        outState.putString(STATE_FIRST, firstName);
        outState.putString(STATE_LAST, lastName);
        outState.putLong(STATE_COOLDOWN, cooldownEndsAt);
    }

    @Override
    protected void onDestroy() {
        if (resendTimer != null) resendTimer.cancel();
        super.onDestroy();
    }

    private void bindViews() {
        flipper = findViewById(R.id.view_flipper_forgot);
        textViewStep = findViewById(R.id.text_view_forgot_step);
        textViewSubtitle = findViewById(R.id.text_view_forgot_subtitle);
        imageViewIcon = findViewById(R.id.image_view_forgot_icon);
        progressSteps = findViewById(R.id.progress_forgot_steps);

        layoutEmail = findViewById(R.id.text_input_layout_forgot_email);
        editTextEmail = findViewById(R.id.edit_text_forgot_email);
        errorEmail = findViewById(R.id.text_view_forgot_email_error);
        buttonSendCode = findViewById(R.id.button_send_reset_link);

        editTextCode = findViewById(R.id.edit_text_forgot_code);
        errorCode = findViewById(R.id.text_view_forgot_code_error);
        buttonVerify = findViewById(R.id.button_verify_reset_code);
        buttonResend = findViewById(R.id.button_resend_reset_code);

        editTextNewPassword = findViewById(R.id.edit_text_reset_new_password);
        editTextConfirm = findViewById(R.id.edit_text_reset_confirm_password);
        layoutConfirm = findViewById(R.id.text_input_layout_reset_confirm_password);
        conditionsView = findViewById(R.id.reset_password_conditions);
        errorPassword = findViewById(R.id.text_view_forgot_password_error);
        buttonReset = findViewById(R.id.button_reset_password);
    }

    private void setListeners() {
        findViewById(R.id.button_forgot_back).setOnClickListener(v -> goBack());
        findViewById(R.id.button_back_to_login).setOnClickListener(v -> backToLogin(null));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        });

        // Step 1
        buttonSendCode.setOnClickListener(v -> sendCode(false));
        editTextEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendCode(false);
                return true;
            }
            return false;
        });
        editTextEmail.addTextChangedListener(new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                layoutEmail.setError(null);
                errorEmail.setVisibility(View.GONE);
            }
        });

        // Step 2
        buttonVerify.setOnClickListener(v -> verifyCode());
        buttonResend.setOnClickListener(v -> sendCode(true));
        editTextCode.addTextChangedListener(new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                errorCode.setVisibility(View.GONE);
                if (!ButtonLoading.isLoading(buttonVerify)) buttonVerify.setEnabled(s.length() == 6);
            }
        });

        // Step 3
        buttonReset.setOnClickListener(v -> resetPassword());
        DebouncedTextWatcher.Simple passwordWatcher = new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                errorPassword.setVisibility(View.GONE);
                refreshPasswordState();
            }
        };
        editTextNewPassword.addTextChangedListener(passwordWatcher);
        editTextConfirm.addTextChangedListener(passwordWatcher);
    }

    // steps

    private void showStep(int newStep) {
        step = newStep;
        flipper.setDisplayedChild(newStep);
        textViewStep.setText(String.format(Locale.getDefault(), getString(R.string.step_of), newStep + 1, 3));
        progressSteps.setProgress(newStep + 1);

        if (newStep == STEP_EMAIL) {
            textViewSubtitle.setText(R.string.forgot_subtitle_email);
            imageViewIcon.setImageResource(R.drawable.ic_mail);
        } else if (newStep == STEP_CODE) {
            textViewSubtitle.setText(getString(R.string.forgot_subtitle_code, LoginFlow.maskEmail(email)));
            imageViewIcon.setImageResource(R.drawable.ic_shield_check);
            editTextCode.requestFocus();
        } else {
            textViewSubtitle.setText(R.string.forgot_subtitle_password);
            imageViewIcon.setImageResource(R.drawable.ic_key);
            refreshPasswordState();
            editTextNewPassword.requestFocus();
        }
    }

    private void goBack() {
        if (step == STEP_EMAIL) {
            finish();
        } else {
            restartFromEmail(null);
        }
    }
    private void restartFromEmail(@Nullable String message) {
        resetToken = null;
        firstName = "";
        lastName = "";
        editTextCode.setText("");
        editTextNewPassword.setText("");
        editTextConfirm.setText("");
        cooldownEndsAt = 0;
        if (resendTimer != null) resendTimer.cancel();
        showStep(STEP_EMAIL);
        if (message != null) showError(errorEmail, message);
    }

    // step 1: email

    private void sendCode(boolean isResend) {
        MaterialButton button = isResend ? buttonResend : buttonSendCode;
        if (ButtonLoading.isLoading(button)) return;
        if (isResend && System.currentTimeMillis() < cooldownEndsAt) return;

        String typed = isResend ? email : text(editTextEmail).trim();
        if (typed.isEmpty()) {
            layoutEmail.setError(getString(R.string.error_email_required));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(typed).matches()) {
            layoutEmail.setError(getString(R.string.error_email_invalid));
            return;
        }

        ButtonLoading.start(button, getString(isResend ? R.string.sending : R.string.sending_code));
        new AuthRepository(this).sendForgotPasswordOtp(typed).enqueue(new SafeCallback<ResponseBody>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(button);
            }

            @Override
            public void onSuccess(@Nullable ResponseBody body) {
                email = typed;
                editTextCode.setText("");
                cooldownEndsAt = System.currentTimeMillis() + RESEND_COOLDOWN_MS;
                if (step != STEP_CODE) showStep(STEP_CODE);
                startCooldown();
                Toast.makeText(ForgotPasswordActivity.this,
                        isResend ? R.string.toast_new_code_sent : R.string.toast_code_sent, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull String message, int code) {
                showError(isResend ? errorCode : errorEmail, message);
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

    // step 2: code

    private void verifyCode() {
        String code = text(editTextCode).trim();
        if (code.length() < 6) {
            showError(errorCode, getString(R.string.error_code_length));
            return;
        }
        if (ButtonLoading.isLoading(buttonVerify)) return;

        ButtonLoading.start(buttonVerify, getString(R.string.verifying));
        new AuthRepository(this).verifyForgotPasswordOtp(email, code).enqueue(new SafeCallback<VerifyResetOtpResponse>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonVerify);
            }

            @Override
            public void onSuccess(@Nullable VerifyResetOtpResponse body) {
                VerifyResetOtpResponse result = body != null ? body.resolve() : null;
                if (result == null || result.getToken() == null || result.getToken().isEmpty()) {
                    editTextCode.setText("");
                    showError(errorCode, getString(R.string.error_code_not_verified));
                    return;
                }
                resetToken = result.getToken();
                firstName = result.getFirstName() != null ? result.getFirstName() : "";
                lastName = result.getLastName() != null ? result.getLastName() : "";
                if (resendTimer != null) resendTimer.cancel();
                showStep(STEP_PASSWORD);
            }

            @Override
            public void onError(@NonNull String message, int code) {
                editTextCode.setText(""); // wrong or expired: clear so they can type again
                showError(errorCode, message);
            }
        });
    }

    // step 3: password

    private boolean refreshPasswordState() {
        String password = text(editTextNewPassword);
        String confirm = text(editTextConfirm);
        List<PasswordRules.Condition> conditions = PasswordRules.evaluate(password, firstName, lastName, null);
        boolean matches = !password.isEmpty() && password.equals(confirm);

        conditionsView.update(conditions, !confirm.isEmpty(), matches);
        layoutConfirm.setError(!confirm.isEmpty() && !matches ? getString(R.string.error_passwords_do_not_match) : null);

        boolean canSubmit = PasswordRules.allMet(conditions) && matches;
        if (!ButtonLoading.isLoading(buttonReset)) buttonReset.setEnabled(canSubmit);
        return canSubmit;
    }

    private void resetPassword() {
        if (resetToken == null) {
            restartFromEmail(getString(R.string.error_reset_session_expired));
            return;
        }
        if (!refreshPasswordState()) {
            showError(errorPassword, getString(R.string.error_password_rules));
            return;
        }
        if (ButtonLoading.isLoading(buttonReset)) return;

        String newPassword = text(editTextNewPassword);
        ButtonLoading.start(buttonReset, getString(R.string.saving));
        new AuthRepository(this).resetPassword(email, resetToken, newPassword).enqueue(new SafeCallback<ResponseBody>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(buttonReset);
                refreshPasswordState();
            }

            @Override
            public void onSuccess(@Nullable ResponseBody body) {
                Toast.makeText(ForgotPasswordActivity.this, R.string.password_changed_toast, Toast.LENGTH_LONG).show();
                backToLogin(getString(R.string.password_changed_login));
            }

            @Override
            public void onError(@NonNull String message, int code) {
                if (isResetSessionError(message)) {
                    restartFromEmail(getString(R.string.error_reset_session_expired));
                    return;
                }
                // e.g. "New password must be different from your current password."
                editTextNewPassword.setText("");
                editTextConfirm.setText("");
                showError(errorPassword, message);
            }
        });
    }

    /** Backend messages that mean the reset token is no longer usable. */
    private static boolean isResetSessionError(String message) {
        String m = message == null ? "" : message.trim().toLowerCase(Locale.ROOT);
        return m.equals("invalid email for password change.") || m.equals("password change failed.")
                || m.contains("token") || m.contains("expired");
    }

    // helpers

    private void backToLogin(@Nullable String notice) {
        Intent intent = new Intent(this, LoginActivity.class);
        if (notice != null) intent.putExtra(LoginActivity.EXTRA_NOTICE, notice);
        String knownEmail = !email.isEmpty() ? email : text(editTextEmail).trim();
        if (!knownEmail.isEmpty()) intent.putExtra(LoginFlow.EXTRA_EMAIL, knownEmail);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void showError(TextView target, String message) {
        target.setText(message);
        target.setVisibility(View.VISIBLE);
    }

    private static String text(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString();
    }
}
