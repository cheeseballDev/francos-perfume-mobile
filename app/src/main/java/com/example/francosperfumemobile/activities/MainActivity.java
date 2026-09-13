package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.fragments.DashboardFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Complete logic
        if (!checkIfLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            this.onDestroy();
        }
        // This shit basically shows how to add fragments to fragment container view. just use if statements on the bottom nar bars
        // read the docs.
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, DashboardFragment.newInstance("", ""))
                    .addToBackStack(null)
                    .commit();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // add logic if top and bottom nav bar are pressed
    }

    //TODO: ADD THE SESSIONTOKEN STORAGE THING
    protected boolean checkIfLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        if (sharedPreferences.getString("SessionToken",null) != null) {
            return false;
        }

        return true;
    }

}