package com.example.francosperfumemobile.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.fragments.DashboardFragment;
import com.example.francosperfumemobile.fragments.DeliveryFragment;
import com.example.francosperfumemobile.fragments.InventoryFragment;
import com.example.francosperfumemobile.fragments.RequestFragment;
import com.example.francosperfumemobile.helpers.FcmTokenHelper;
import com.example.francosperfumemobile.helpers.InsetsHelper;
import com.example.francosperfumemobile.helpers.NotificationHelper;
import com.example.francosperfumemobile.helpers.NotificationRouter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main screen with the four tabs. The bottom bar and the drawer always show the same tab.
 * Also asks for the notification permission once, and opens the request/delivery a tapped
 * push notification points to.
 */
public class MainActivity extends BaseActivity {

    /** Old extra name, kept so existing callers still work. */
    public static final String EXTRA_TAB = "PREVIOUS_ACTIVE_FRAGMENT";
    public static final String TAB_DASHBOARD = "DASHBOARD";
    public static final String TAB_INVENTORY = "INVENTORY";
    public static final String TAB_REQUEST = "REQUEST";
    public static final String TAB_DELIVERY = "DELIVERY";

    private static final String STATE_TAB = "current_tab";
    private static final String PREFS_UI = "francos_perfume_ui";
    private static final String KEY_ASKED_NOTIFICATIONS = "asked_notifications";

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private TextView toolbarTitle;
    private String currentTab = TAB_DASHBOARD;

    private final ActivityResultLauncher<String> notificationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) FcmTokenHelper.registerCurrentToken(this);
            });

    public static Intent newIntent(Context context, String tab) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TAB, tab);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) return;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        drawerLayout = findViewById(R.id.drawerlayout_main);
        View topToolbar = findViewById(R.id.top_navigation_bar_main);
        toolbarTitle = topToolbar.findViewById(R.id.text_view_toolbar_title);

        InsetsHelper.applyEdgeToEdge(drawerLayout, topToolbar, bottomNavigationView, false,
                findViewById(R.id.include_left_drawer));

        setupDrawer(drawerLayout);
        setupTopBar(topToolbar, drawerLayout);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            String tab = tabForMenuId(item.getItemId());
            if (tab == null) return false;
            showTab(tab, false);
            return true;
        });
        // Tapping the current tab again scrolls/refreshes nothing; just keep it
        bottomNavigationView.setOnItemReselectedListener(item -> { });

        if (savedInstanceState == null) {
            String tab = getIntent().getStringExtra(EXTRA_TAB);
            selectTab(tab != null ? tab : TAB_DASHBOARD);
            FcmTokenHelper.registerCurrentToken(this);
            NotificationHelper.createChannel(this);
            askForNotificationPermission();
            openNotificationTarget(getIntent());
        } else {
            currentTab = savedInstanceState.getString(STATE_TAB, TAB_DASHBOARD);
            updateTitle(currentTab);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TAB, currentTab);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String tab = intent.getStringExtra(EXTRA_TAB);
        if (tab != null) selectTab(tab);
        openNotificationTarget(intent);
    }

    @Override
    protected void onDrawerNavigate(String tab) {
        selectTab(tab);
    }

    /** Switches tab and keeps the bottom bar in sync (fixes the old PREVIOUS_ACTIVE_FRAGMENT mismatch). */
    public void selectTab(String tab) {
        int menuId = menuIdForTab(tab);
        if (bottomNavigationView.getSelectedItemId() == menuId && getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) != null) {
            return;
        }
        // This triggers the listener above, which loads the fragment exactly once
        bottomNavigationView.setSelectedItemId(menuId);
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) == null) {
            showTab(tab, true);
        }
    }

    private void showTab(String tab, boolean force) {
        if (!force && tab.equals(currentTab) && getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) != null) {
            return;
        }
        currentTab = tab;
        updateTitle(tab);
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, fragmentFor(tab), tab)
                .commit();
    }

    private void updateTitle(String tab) {
        if (toolbarTitle == null) return;
        switch (tab) {
            case TAB_INVENTORY:
                toolbarTitle.setText(R.string.inventory_text);
                break;
            case TAB_REQUEST:
                toolbarTitle.setText(R.string.requests_text);
                break;
            case TAB_DELIVERY:
                toolbarTitle.setText(R.string.deliveries_text);
                break;
            default:
                toolbarTitle.setText(R.string.title);
        }
    }

    private static Fragment fragmentFor(String tab) {
        switch (tab) {
            case TAB_INVENTORY:
                return new InventoryFragment();
            case TAB_REQUEST:
                return new RequestFragment();
            case TAB_DELIVERY:
                return new DeliveryFragment();
            default:
                return new DashboardFragment();
        }
    }

    @IdRes
    private static int menuIdForTab(String tab) {
        if (TAB_INVENTORY.equals(tab)) return R.id.inventory;
        if (TAB_REQUEST.equals(tab)) return R.id.request;
        if (TAB_DELIVERY.equals(tab)) return R.id.delivery;
        return R.id.dashboard;
    }

    private static String tabForMenuId(int id) {
        if (id == R.id.dashboard) return TAB_DASHBOARD;
        if (id == R.id.inventory) return TAB_INVENTORY;
        if (id == R.id.request) return TAB_REQUEST;
        if (id == R.id.delivery) return TAB_DELIVERY;
        return null;
    }

    /** A tapped push notification arrives here with its type and reference id. */
    private void openNotificationTarget(Intent intent) {
        if (intent == null) return;
        String type, referenceId;
        if (intent.hasExtra(NotificationRouter.EXTRA_TYPE)) {
            // Notification shown by our own NotificationHelper (app was open)
            type = intent.getStringExtra(NotificationRouter.EXTRA_TYPE);
            referenceId = intent.getStringExtra(NotificationRouter.EXTRA_REFERENCE_ID);
            intent.removeExtra(NotificationRouter.EXTRA_TYPE);
            intent.removeExtra(NotificationRouter.EXTRA_REFERENCE_ID);
        } else if (intent.hasExtra("type") && intent.hasExtra("google.message_id")) {
            // Notification shown by Android while the app was closed: the data payload arrives as extras
            type = intent.getStringExtra("type");
            referenceId = intent.getStringExtra("referenceId");
            intent.removeExtra("type");
            intent.removeExtra("google.message_id");
        } else {
            return;
        }

        Intent target = NotificationRouter.intentFor(this, type, referenceId);
        if (target.getComponent() != null && MainActivity.class.getName().equals(target.getComponent().getClassName())) {
            String tab = target.getStringExtra(EXTRA_TAB);
            if (tab != null) selectTab(tab);
        } else {
            startActivity(target);
        }
    }

    /** Android 13+: ask once for permission to show push notifications. */
    private void askForNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return;

        SharedPreferences prefs = getSharedPreferences(PREFS_UI, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_ASKED_NOTIFICATIONS, false)) return;
        prefs.edit().putBoolean(KEY_ASKED_NOTIFICATIONS, true).apply();
        notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
    }
}
