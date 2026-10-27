package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.francosperfumemobile.activities.DeliveryDetailsActivity;
import com.example.francosperfumemobile.activities.MainActivity;
import com.example.francosperfumemobile.activities.RequestDetailsActivity;

import java.util.Locale;

/**
 * Where a notification leads. Same rules as the website's NotificationDropdown:
 * DELIVERY → delivery details, REQUEST → request details, INVENTORY / LOW_STOCK → inventory.
 */
public final class NotificationRouter {

    public static final String EXTRA_TYPE = "NOTIFICATION_TYPE";
    public static final String EXTRA_REFERENCE_ID = "NOTIFICATION_REFERENCE_ID";

    private NotificationRouter() {
    }

    public static Intent intentFor(Context context, @Nullable String type, @Nullable String referenceId) {
        String t = type == null ? "" : type.toUpperCase(Locale.ROOT);
        Integer id = parseId(referenceId);

        if (t.contains("DELIVERY")) {
            return id != null
                    ? DeliveryDetailsActivity.newIntent(context, id)
                    : MainActivity.newIntent(context, MainActivity.TAB_DELIVERY);
        }
        if (t.contains("REQUEST")) {
            return id != null
                    ? RequestDetailsActivity.newIntent(context, id)
                    : MainActivity.newIntent(context, MainActivity.TAB_REQUEST);
        }
        if (t.contains("INVENTORY") || t.contains("LOW_STOCK") || t.contains("STOCK")) {
            return MainActivity.newIntent(context, MainActivity.TAB_INVENTORY);
        }
        return MainActivity.newIntent(context, MainActivity.TAB_DASHBOARD);
    }

    @Nullable
    private static Integer parseId(@Nullable String referenceId) {
        if (referenceId == null) return null;
        try {
            int id = Integer.parseInt(referenceId.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
