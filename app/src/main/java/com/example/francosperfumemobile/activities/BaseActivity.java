package com.example.francosperfumemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.repository.NotificationRepository;
import com.example.francosperfumemobile.backend.responses.NotificationListResponse;
import com.example.francosperfumemobile.backend.retrofit.SessionManager;
import com.example.francosperfumemobile.helpers.AccessRules;
import com.example.francosperfumemobile.helpers.DialogHelper;
import com.example.francosperfumemobile.helpers.FcmTokenHelper;
import com.example.francosperfumemobile.helpers.NavigationHelper;
import com.example.francosperfumemobile.helpers.StatusStyle;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Shared by every screen after login:
 *  - sends the user to the login screen when there is no valid session,
 *  - fills the navigation drawer (name, role, branch, logout),
 *  - wires the top bar (menu / back, notifications bell with unread count).
 *
 * Subclasses should return early when {@link #isFinishing()} is true after super.onCreate().
 */
public class BaseActivity extends AppCompatActivity {

    protected SessionManager sessionManager;
    private TextView notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn() || !AccessRules.canUseMobileApp(sessionManager.getRole())) {
            sessionManager.clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUnreadBadge();
    }

    // ------------------------------------------------------------------ drawer

    protected void setupDrawer(@Nullable DrawerLayout drawerLayout) {
        if (drawerLayout == null) return;

        TextView name = drawerLayout.findViewById(R.id.text_view_drawer_name);
        TextView email = drawerLayout.findViewById(R.id.text_view_drawer_email);
        TextView role = drawerLayout.findViewById(R.id.text_view_drawer_role);
        TextView branch = drawerLayout.findViewById(R.id.text_view_drawer_branch);

        if (name != null) name.setText(sessionManager.getFullName());
        if (email != null) email.setText(sessionManager.getEmail());
        if (role != null) StatusStyle.apply(role, "ROLE", StatusStyle.label(sessionManager.getRole()));
        if (branch != null) {
            String location = sessionManager.getBranchLocation();
            branch.setText(location != null ? location : getString(R.string.no_branch));
        }

        bindDrawerNav(drawerLayout, R.id.drawer_nav_dashboard, MainActivity.TAB_DASHBOARD);
        bindDrawerNav(drawerLayout, R.id.drawer_nav_inventory, MainActivity.TAB_INVENTORY);
        bindDrawerNav(drawerLayout, R.id.drawer_nav_request, MainActivity.TAB_REQUEST);
        bindDrawerNav(drawerLayout, R.id.drawer_nav_delivery, MainActivity.TAB_DELIVERY);

        View notifications = drawerLayout.findViewById(R.id.drawer_nav_notifications);
        if (notifications != null) {
            notifications.setOnClickListener(v -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                openNotifications();
            });
        }

        View logout = drawerLayout.findViewById(R.id.button_logout);
        if (logout != null) {
            logout.setOnClickListener(v -> DialogHelper.confirm(this,
                    getString(R.string.logout_confirm_title),
                    getString(R.string.logout_confirm_message),
                    getString(R.string.logout_text),
                    true,
                    this::logout));
        }

        NavigationHelper.setupDrawerBackButton(this, drawerLayout);
    }

    private void bindDrawerNav(DrawerLayout drawerLayout, int viewId, String tab) {
        View item = drawerLayout.findViewById(viewId);
        if (item == null) return;
        item.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            onDrawerNavigate(tab);
        });
    }

    /** MainActivity switches tabs; other screens open MainActivity on that tab. */
    protected void onDrawerNavigate(String tab) {
        startActivity(MainActivity.newIntent(this, tab));
        finish();
    }

    protected void logout() {
        FcmTokenHelper.deleteToken(); // stop pushes for this user on this phone
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // ----------------------------------------------------------------- top bar

    /** Top bar of the main tabs: menu opens the drawer, bell opens notifications. */
    protected void setupTopBar(@Nullable View topBar, @Nullable DrawerLayout drawerLayout) {
        if (topBar == null) return;
        View menu = topBar.findViewById(R.id.button_menu);
        if (menu != null) {
            if (drawerLayout != null) {
                menu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
            } else {
                menu.setVisibility(View.GONE);
            }
        }
        bindBell(topBar);
    }

    /** Top bar of detail screens: back arrow, title, bell. */
    protected void setupDetailTopBar(@Nullable View topBar, String title, @Nullable String subtitle) {
        if (topBar == null) return;
        View back = topBar.findViewById(R.id.button_back);
        if (back != null) back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        setDetailTitle(topBar, title, subtitle);
        bindBell(topBar);
    }

    protected void setDetailTitle(@Nullable View topBar, @Nullable String title, @Nullable String subtitle) {
        if (topBar == null) return;
        TextView titleView = topBar.findViewById(R.id.text_view_detail_title);
        TextView subtitleView = topBar.findViewById(R.id.text_view_detail_subtitle);
        if (titleView != null && title != null) titleView.setText(title);
        if (subtitleView != null) {
            subtitleView.setText(subtitle);
            subtitleView.setVisibility(subtitle == null || subtitle.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    private void bindBell(View topBar) {
        View bell = topBar.findViewById(R.id.button_notification);
        notificationBadge = topBar.findViewById(R.id.text_view_notification_badge);
        if (bell != null) bell.setOnClickListener(v -> openNotifications());
    }

    protected void openNotifications() {
        startActivity(new Intent(this, NotificationActivity.class));
    }

    /** Asks the backend for the unread count and updates the red badge on the bell. */
    protected void refreshUnreadBadge() {
        if (notificationBadge == null || sessionManager == null || !sessionManager.isLoggedIn()) return;
        new NotificationRepository(this).getNotifications().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (isFinishing() || isDestroyed() || !response.isSuccessful()) return;
                setUnreadCount(NotificationListResponse.from(response.body()).getUnreadCount());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                // Badge is optional; keep the old value
            }
        });
    }

    protected void setUnreadCount(int count) {
        if (notificationBadge == null) return;
        if (count <= 0) {
            notificationBadge.setVisibility(View.GONE);
        } else {
            notificationBadge.setText(count > 9 ? "9+" : String.valueOf(count));
            notificationBadge.setVisibility(View.VISIBLE);
        }
        View bell = notificationBadge.getRootView().findViewById(R.id.button_notification);
        if (bell != null) {
            bell.setContentDescription(count > 0
                    ? getResources().getQuantityString(R.plurals.cd_notifications_unread, count, count)
                    : getString(R.string.cd_notifications));
        }
    }
}
