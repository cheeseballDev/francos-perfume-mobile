package com.example.francosperfumemobile.helpers;

import android.view.View;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class NavigationHelper {

    public static void setupDrawerBackButton(ComponentActivity activity, DrawerLayout drawerLayout) {
        OnBackPressedCallback backCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        };

        activity.getOnBackPressedDispatcher().addCallback(activity, backCallback);

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                backCallback.setEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                backCallback.setEnabled(false);
            }
        });
    }
}