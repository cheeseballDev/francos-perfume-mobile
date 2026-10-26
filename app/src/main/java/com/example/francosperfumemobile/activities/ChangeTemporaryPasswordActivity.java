package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.repository.AuthRepository;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResultResponse;
import com.example.francosperfumemobile.components.PasswordConditionsView;
import com.example.francosperfumemobile.helpers.ButtonLoading;
import com.example.francosperfumemobile.helpers.DebouncedTextWatcher;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.LoginFlow;
import com.example.francosperfumemobile.helpers.PasswordRules;
import com.example.francosperfumemobile.helpers.SafeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

/**
 * Shown when login says requiresPasswordChange (temporary / default password).
 * Same rules as the website's ForcePasswordChangeModal, including "different from the
 * temporary password" and "must not include your name".
 * Saving calls login again with NewPassword (website: handlePasswordUpdate); the reply is then
 * handled like a normal login, so an OTP account goes on to the code screen.
 */
public class ChangeTemporaryPasswordActivity extends AppCompatActivity {

    private String email, temporaryPassword, firstName, lastName;
    private TextInputEditText editTextNew, editTextConfirm;
    private TextInputLayout layoutConfirm;
    private PasswordConditionsView conditionsView;
    private TextView textViewError;
    private MaterialButton buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_temporary_password);
        InsetsHelper.applySystemBarsAndIme(findViewById(R.id.change_temporary_password));

        Intent intent = getIntent();
        email = intent.getStringExtra(LoginFlow.EXTRA_EMAIL);
        temporaryPassword = intent.getStringExtra(LoginFlow.EXTRA_PASSWORD);
        firstName = intent.getStringExtra(LoginFlow.EXTRA_FIRST_NAME);
        lastName = intent.getStringExtra(LoginFlow.EXTRA_LAST_NAME);
        if (email == null || temporaryPassword == null) {
            finish();
            return;
        }

        editTextNew = findViewById(R.id.edit_text_new_password);
        editTextConfirm = findViewById(R.id.edit_text_confirm_password);
        layoutConfirm = findViewById(R.id.text_input_layout_confirm_password);
        conditionsView = findViewById(R.id.password_conditions);
        textViewError = findViewById(R.id.text_view_change_password_error);
        buttonSave = findViewById(R.id.button_save_new_password);

        findViewById(R.id.button_change_password_back).setOnClickListener(v -> finish());
        buttonSave.setOnClickListener(v -> save());

        DebouncedTextWatcher.Simple watcher = new DebouncedTextWatcher.Simple() {
            @Override
            public void afterTextChanged(Editable s) {
                textViewError.setVisibility(View.GONE);
                refresh();
            }
        };
        editTextNew.addTextChangedListener(watcher);
        editTextConfirm.addTextChangedListener(watcher);
        refresh();
    }

    private boolean refresh() {
        String password = text(editTextNew);
        String confirm = text(editTextConfirm);
        List<PasswordRules.Condition> conditions = PasswordRules.evaluate(password, firstName, lastName, temporaryPassword);
        boolean matches = !password.isEmpty() && password.equals(confirm);

        conditionsView.update(conditions, !confirm.isEmpty(), matches);
        layoutConfirm.setError(!confirm.isEmpty() && !matches ? getString(R.string.error_passwords_do_not_match) : null);

        boolean canSubmit = PasswordRules.allMet(conditions) && matches;
        if (!ButtonLoading.isLoading(buttonSave)) buttonSave.setEnabled(canSubmit);
        return canSubmit;
    }

    private void save() {
        if (!refresh() || ButtonLoading.isLoading(buttonSave)) return;
        String newPassword = text(editTextNew);

        ButtonLoading.start(buttonSave, getString(R.string.saving));
        new AuthRepository(this).changeTemporaryPassword(email, temporaryPassword, newPassword)
                .enqueue(new SafeCallback<LoginResultResponse<LoginResponse>>(this) {
                    @Override
                    public void onFinish() {
                        ButtonLoading.stop(buttonSave);
                        refresh();
                    }

                    @Override
                    public void onSuccess(@Nullable LoginResultResponse<LoginResponse> body) {
                        LoginResponse data = body != null ? body.getData() : null;
                        if (data == null || (data.getAccessToken() == null && !data.isRequiresOTP())) {
                            // Password saved but no session: send them to log in with the new one
                            Intent login = new Intent(ChangeTemporaryPasswordActivity.this, LoginActivity.class);
                            login.putExtra(LoginActivity.EXTRA_NOTICE, getString(R.string.password_updated_login));
                            login.putExtra(LoginFlow.EXTRA_EMAIL, email);
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(login);
                            finish();
                            return;
                        }
                        // The temporary password is no longer valid; carry on as a normal login
                        data.setRequiresPasswordChange(false);
                        String error = LoginFlow.continueLogin(ChangeTemporaryPasswordActivity.this, data, email, newPassword);
                        if (error != null) {
                            showError(error);
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onError(@NonNull String message, int code) {
                        showError(message);
                    }
                });
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
    }

    private static String text(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString();
    }
}
