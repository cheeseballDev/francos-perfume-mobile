package com.example.francosperfumemobile.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.francosperfumemobile.R;

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

        initializeUI();

        //TODO: LOGIN LOGIC HERE
    }
    private void initializeUI() {
        loginButton = findViewById(R.id.button_login);
        forgotPasswordButton = findViewById(R.id.button_forgot_password);
        emailTextField = findViewById(R.id.textfield_email);
        passwordTextField = findViewById(R.id.textfield_password);

        setListeners();
    }

    private void setListeners() {
        forgotPasswordButton.setOnClickListener(v -> {

        });

        loginButton.setOnClickListener(v -> {

        });
    }
}