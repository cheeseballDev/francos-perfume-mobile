package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.components.DashboardCardView;
import com.example.francosperfumemobile.fragments.DashboardFragment;
import com.example.francosperfumemobile.fragments.DeliveryFragment;
import com.example.francosperfumemobile.fragments.InventoryFragment;
import com.example.francosperfumemobile.fragments.RequestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "francos_perfume_session";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private BottomNavigationView bottomNavigationView;
    private DashboardCardView inventoryCard, requestCard, dispatchCard, deliveryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //TODO: Ask opinion how the hiding of systemUI would look like
        //hideSystemUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
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

        inventoryCard = findViewById(R.id.dashboard_inventory_card);
        requestCard = findViewById(R.id.dashboard_requests_card);
        dispatchCard = findViewById(R.id.dashboard_dispatch_card);
        deliveryCard = findViewById(R.id.dashboard_delivery_card);
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
    }

    /*
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
    */
}