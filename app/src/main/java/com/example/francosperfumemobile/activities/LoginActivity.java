package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.repository.AuthRepository;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResultResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.example.francosperfumemobile.helpers.AccessRules;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.LoginFlow;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.example.francosperfumemobile.helpers.SessionEvents;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Login. After the backend answers, LoginFlow decides the next screen:
 * temporary password → change it, OTP → enter the code, otherwise → the app.
 */
public class LoginActivity extends AppCompatActivity {

    /** Green message shown above the button, e.g. after a password reset. */
    public static final String EXTRA_NOTICE = "LOGIN_NOTICE";

    private MaterialButton loginButton, forgotPasswordButton;
    private TextInputEditText emailTextField, passwordTextField;
    private TextInputLayout emailLayout, passwordLayout;
    private TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        InsetsHelper.applySystemBarsAndIme(findViewById(R.id.login));

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            if (AccessRules.canUseMobileApp(sessionManager.getRole())) {
                Intent intent = new Intent(this, MainActivity.class);
                if (getIntent() != null && getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras()); // keep notification extras
                }
                startActivity(intent);
                finish();
                return;
            }
            // A session from before the role check existed; make them log in again
            sessionManager.clearSession();
        }

        initializeUI();
        showIntentMessage(getIntent());
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showIntentMessage(intent);
    }

    private void initializeUI() {
        loginButton = findViewById(R.id.button_login);
        forgotPasswordButton = findViewById(R.id.button_forgot_password);
        emailTextField = findViewById(R.id.edit_text_email);
        passwordTextField = findViewById(R.id.edit_text_password);
        emailLayout = findViewById(R.id.text_input_layout_email);
        passwordLayout = findViewById(R.id.text_input_layout_password);
        textViewMessage = findViewById(R.id.text_view_login_error);

        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            intent.putExtra(LoginFlow.EXTRA_EMAIL, textOf(emailTextField));
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> login());

        passwordTextField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
            return false;
        });

        DebouncedTextWatcher.Simple clearErrors = new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                emailLayout.setError(null);
                passwordLayout.setError(null);
                hideMessage();
            }
        };
        emailTextField.addTextChangedListener(clearErrors);
        passwordTextField.addTextChangedListener(clearErrors);
    }

    private void showIntentMessage(@Nullable Intent intent) {
        if (intent == null || textViewMessage == null) return;
        if (intent.getBooleanExtra(SessionEvents.EXTRA_SESSION_EXPIRED, false)) {
            showMessage(getString(R.string.error_session_expired), true);
            intent.removeExtra(SessionEvents.EXTRA_SESSION_EXPIRED);
        } else if (intent.hasExtra(EXTRA_NOTICE)) {
            showMessage(intent.getStringExtra(EXTRA_NOTICE), false);
            intent.removeExtra(EXTRA_NOTICE);
        }
        String email = intent.getStringExtra(LoginFlow.EXTRA_EMAIL);
        if (email != null && !email.isEmpty() && emailTextField != null && textOf(emailTextField).isEmpty()) {
            emailTextField.setText(email);
        }
    }

    private void login() {
        if (ButtonLoading.isLoading(loginButton)) return; // ignore double taps

        String email = textOf(emailTextField);
        String password = passwordTextField.getText() == null ? "" : passwordTextField.getText().toString();

        boolean valid = true;
        if (email.isEmpty()) {
            emailLayout.setError(getString(R.string.error_email_required));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.error_email_invalid));
            valid = false;
        }
        if (password.trim().isEmpty()) {
            passwordLayout.setError(getString(R.string.error_password_required));
            valid = false;
        }
        if (!valid) return;

        hideMessage();
        ButtonLoading.start(loginButton, getString(R.string.logging_in));

        new AuthRepository(this).login(email, password).enqueue(new SafeCallback<LoginResultResponse<LoginResponse>>(this) {
            @Override
            public void onFinish() {
                ButtonLoading.stop(loginButton);
            }

            @Override
            public void onSuccess(@Nullable LoginResultResponse<LoginResponse> body) {
                LoginResponse data = body != null ? body.getData() : null;
                if (data == null) {
                    String message = body != null && body.getMessage() != null ? body.getMessage() : getString(R.string.error_login_generic);
                    showMessage(message, true);
                    return;
                }
                String error = LoginFlow.continueLogin(LoginActivity.this, data, email, password);
                if (error != null) {
                    showMessage(error, true);
                } else {
                    // Moving on to the OTP / new password screen: don't leave the password behind
                    passwordTextField.setText("");
                }
            }

            @Override
            public void onError(@NonNull String message, int code) {
                // 401 here means wrong email or password, not an expired session
                showMessage(code == 401 && message.startsWith("Your session") ? getString(R.string.error_invalid_credentials) : message, true);
            }
        });
    }

    private void showMessage(String message, boolean isError) {
        textViewMessage.setText(message);
        textViewMessage.setTextColor(ContextCompat.getColor(this, isError ? R.color.fp_destructive : R.color.tone_success_text));
        textViewMessage.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        if (textViewMessage != null) textViewMessage.setVisibility(View.GONE);
    }

    private static String textOf(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
}
