package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;

public class BaseActivity extends AppCompatActivity  {
    protected SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
    }

    protected void setupDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout != null) {
            TextView textViewBranch = drawerLayout.findViewById(R.id.text_view_drawer_branch);
            if (textViewBranch != null) {
                textViewBranch.setText(sessionManager.getBranchLocation());
            }

            AppCompatButton buttonLogout = drawerLayout.findViewById(R.id.button_logout);
            if (buttonLogout != null) {
                buttonLogout.setOnClickListener(v -> {
                    sessionManager.clearSession();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            }
        }
    }
}
