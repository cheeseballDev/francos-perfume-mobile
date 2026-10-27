package com.example.francosperfumemobile.helpers;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Edge-to-edge padding. The app draws behind the status and navigation bars; these helpers
 * pad the right views so nothing sits under the system bars or the keyboard.
 */
public final class InsetsHelper {

    private InsetsHelper() {
    }

    /** Pads the whole view for the system bars, and for the keyboard when it is open. Used by the login screens. */
    public static void applySystemBarsAndIme(View root) {
        if (root == null) return;
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, Math.max(bars.bottom, ime.bottom));
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /** Pads the whole view for the system bars only. */
    public static void applySystemBars(View root) {
        if (root == null) return;
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /**
     * Lets the top bar and the bottom bar colour run under the status / navigation bars:
     * the top inset becomes extra top padding on {@code topBar}, the bottom inset extra bottom
     * padding on {@code bottomBar} (or on the root when there is none).
     *
     * @param includeIme also lift the bottom bar above the keyboard (detail screens with inputs)
     * @param fullHeight views that need both (e.g. the navigation drawer panel)
     */
    public static void applyEdgeToEdge(View root, @Nullable View topBar, @Nullable View bottomBar,
                                       boolean includeIme, View... fullHeight) {
        if (root == null) return;
        final int topStart = topBar != null ? topBar.getPaddingTop() : 0;
        final int bottomStart = bottomBar != null ? bottomBar.getPaddingBottom() : 0;
        final int[] fullTop = new int[fullHeight.length];
        final int[] fullBottom = new int[fullHeight.length];
        for (int i = 0; i < fullHeight.length; i++) {
            fullTop[i] = fullHeight[i] != null ? fullHeight[i].getPaddingTop() : 0;
            fullBottom[i] = fullHeight[i] != null ? fullHeight[i].getPaddingBottom() : 0;
        }

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            int bottom = bars.bottom;
            if (includeIme) bottom = Math.max(bottom, insets.getInsets(WindowInsetsCompat.Type.ime()).bottom);

            v.setPadding(bars.left, topBar == null ? bars.top : 0, bars.right, bottomBar == null ? bottom : 0);
            if (topBar != null) {
                topBar.setPadding(topBar.getPaddingLeft(), topStart + bars.top, topBar.getPaddingRight(), topBar.getPaddingBottom());
            }
            if (bottomBar != null) {
                bottomBar.setPadding(bottomBar.getPaddingLeft(), bottomBar.getPaddingTop(), bottomBar.getPaddingRight(), bottomStart + bottom);
            }
            for (int i = 0; i < fullHeight.length; i++) {
                View view = fullHeight[i];
                if (view == null) continue;
                view.setPadding(view.getPaddingLeft(), fullTop[i] + bars.top, view.getPaddingRight(), fullBottom[i] + bars.bottom);
            }
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
