package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.example.francosperfumemobile.fragments.DashboardFragment;
import com.example.francosperfumemobile.fragments.DeliveryFragment;
import com.example.francosperfumemobile.fragments.InventoryFragment;
import com.example.francosperfumemobile.fragments.RequestFragment;
import com.example.francosperfumemobile.helpers.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private View topToolbar;
    private ImageButton buttonMenu, buttonNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
            if (getIntent() != null && getIntent().hasExtra("PREVIOUS_ACTIVE_FRAGMENT")) {
                handlePreviousActiveFragment(getIntent());
            } else {
                loadFragment(new DashboardFragment());
            }
        }

        initializeUI();
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handlePreviousActiveFragment(intent);
    }

    private void handlePreviousActiveFragment(Intent intent) {
        if (intent != null && intent.hasExtra("PREVIOUS_ACTIVE_FRAGMENT")) {
            String target = intent.getStringExtra("PREVIOUS_ACTIVE_FRAGMENT");
            if ("INVENTORY".equals(target)) {
                loadFragment(new InventoryFragment());
            } else if ("REQUEST".equals(target)) {
                loadFragment(new RequestFragment());
            } else if ("DELIVERY".equals(target)) {
                loadFragment(new DeliveryFragment());
            } else {
                loadFragment(new DashboardFragment());
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void initializeUI() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        drawerLayout = findViewById(R.id.drawerlayout_main);
        topToolbar = findViewById(R.id.top_navigation_bar_main);
        buttonMenu = topToolbar.findViewById(R.id.button_menu);
        buttonNotification = topToolbar.findViewById(R.id.button_notification);

        setupDrawer(drawerLayout);

        initializeListeners();
    }

    private void initializeListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.dashboard) {
                return loadFragment(new DashboardFragment());
            } else if (id == R.id.inventory) {
                return loadFragment(new InventoryFragment());
            } else if (id == R.id.request) {
                return loadFragment(new RequestFragment());
            } else if (id == R.id.delivery) {
                return loadFragment(new DeliveryFragment());
            }
            return false;
        });

        buttonMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        buttonNotification.setOnClickListener(v -> {
            Toast.makeText(this, "To be implemented", Toast.LENGTH_SHORT).show();
        });

        NavigationHelper.setupDrawerBackButton(this, drawerLayout);
    }


    private void hideSystemUI() {
        // Tell the window to layout content edge-to-edge behind the bars
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        // Hide both the Status Bar and Navigation Bar
        controller.hide(WindowInsetsCompat.Type.systemBars());

        // Allow user to reveal bars temporarily with a edge swipe
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
    }

}