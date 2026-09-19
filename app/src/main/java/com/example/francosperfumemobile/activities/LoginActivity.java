package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.logindtos.LoginDTO;
import com.example.francosperfumemobile.backend.repository.AuthRepository;
import com.example.francosperfumemobile.backend.responses.loginresponses.LoginResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, forgotPasswordButton;
    private EditText emailTextField, passwordTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SessionManager sm = new SessionManager(this);

        if (sm.isLoggedIn()) {
            if (sm.getRequiresPwChange()) {
                Toast.makeText(this, "Change your password", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ForgotPasswordActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }

        initializeUI();

        //TODO: LOGIN LOGIC HERE
    }
    private void initializeUI() {
        loginButton = findViewById(R.id.button_login);
        forgotPasswordButton = findViewById(R.id.button_forgot_password);
        emailTextField = findViewById(R.id.edit_text_email);
        passwordTextField = findViewById(R.id.edit_text_password);

        setListeners();
    }

    private void setListeners() {
        forgotPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        loginButton.setOnClickListener(v -> {
            login();
        });
    }

    private void login(){
        LoginDTO dto = new LoginDTO();
        dto.setEmail(emailTextField.getText().toString().trim());
        dto.setPassword(passwordTextField.getText().toString());

        AuthRepository repository = new AuthRepository(this);

        repository.login(dto).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse login = response.body();
                    SessionManager sessionManager = new SessionManager(LoginActivity.this);

                    sessionManager.createSession(
                            login.getEmployeeId(),
                            login.getEmail(),
                            login.getAccessToken(),
                            login.getRole(),
                            login.getBranchId(),
                            login.isRequiresPasswordChange(),
                            login.isRequiresOTP()
                    );
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}