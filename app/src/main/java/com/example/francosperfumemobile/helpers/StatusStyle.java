package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.example.francosperfumemobile.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Status badges that look like the website's StatusBadge component
 * (src/utils/statusUtils.js): same status → tone mapping, same colours, light and dark.
 */
public final class StatusStyle {

    public enum Tone { SUCCESS, PROGRESS, PENDING, PARTIAL, DANGER, INBOUND, OUTBOUND, NEUTRAL }

    private static final Map<String, Tone> EXACT = new HashMap<>();

    static {
        for (String s : new String[]{"COMPLETED", "COMPLETE", "APPROVED", "ACCEPTED", "RECEIVED", "DELIVERED", "ACTIVE"})
            EXACT.put(s, Tone.SUCCESS);
        for (String s : new String[]{"IN TRANSIT", "TRANSIT", "DISPATCHED", "ASSIGNED TO DELIVERY"})
            EXACT.put(s, Tone.PROGRESS);
        for (String s : new String[]{"PARTIAL", "PARTIALLY COMPLETED", "PARTIALLY RECEIVED", "CLOSED PARTIAL", "TEMPORARY"})
            EXACT.put(s, Tone.PARTIAL);
        for (String s : new String[]{"REJECTED", "CANCELLED", "CANCELED", "DELIVERY CANCELLED", "DEACTIVATED"})
            EXACT.put(s, Tone.DANGER);
        for (String s : new String[]{"CLOSED", "FINISHED", "BYPASSED", "INACTIVE", "ARCHIVED"})
            EXACT.put(s, Tone.NEUTRAL);
        for (String s : new String[]{"SOURCE BRANCH MANAGER", "DESTINATION BRANCH MANAGER", "OWNER"})
            EXACT.put(s, Tone.PENDING);
        EXACT.put("INBOUND", Tone.INBOUND);
        EXACT.put("OUTBOUND", Tone.OUTBOUND);
    }

    private StatusStyle() {
    }

    public static String normalize(@Nullable String raw) {
        if (raw == null) return "";
        return raw.replace('_', ' ').trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    public static Tone toneOf(@Nullable String raw) {
        String s = normalize(raw);
        Tone tone = EXACT.get(s);
        if (tone != null) return tone;
        if (s.startsWith("PENDING") || s.endsWith("REVIEW") || s.equals("CREATED") || s.equals("FOR DISPATCH")) {
            return Tone.PENDING;
        }
        return Tone.NEUTRAL;
    }

    /** "IN TRANSIT" → "In transit" (website lower-cases then capitalises). */
    public static String label(@Nullable String raw) {
        String s = normalize(raw).toLowerCase(Locale.ROOT);
        if (s.isEmpty()) return "Unknown";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /** Style a TextView as a status badge and set its text. */
    public static void apply(@Nullable TextView view, @Nullable String status) {
        apply(view, status, null);
    }

    public static void apply(@Nullable TextView view, @Nullable String status, @Nullable String customLabel) {
        if (view == null) return;
        Tone tone = toneOf(status);
        view.setText(customLabel != null ? customLabel : label(status));
        applyTone(view, tone, iconFor(status, tone));
    }

    /** Badge with any text, e.g. "Low stock" with the PENDING tone. */
    public static void applyTone(@Nullable TextView view, Tone tone, @DrawableRes int iconRes) {
        if (view == null) return;
        Context context = view.getContext();
        int bg = ContextCompat.getColor(context, bgColor(tone));
        int text = ContextCompat.getColor(context, textColor(tone));
        int border = ContextCompat.getColor(context, borderColor(tone));

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(dp(context, 6));
        shape.setColor(bg);
        shape.setStroke(Math.max(1, Math.round(dp(context, 1))), border);
        view.setBackground(shape);
        view.setTextColor(text);

        if (iconRes != 0) {
            Drawable icon = ContextCompat.getDrawable(context, iconRes);
            if (icon != null) {
                icon = icon.mutate();
                int size = Math.round(dp(context, 12));
                icon.setBounds(0, 0, size, size);
                view.setCompoundDrawablesRelative(icon, null, null, null);
                TextViewCompat.setCompoundDrawableTintList(view, ColorStateList.valueOf(text));
            }
        } else {
            view.setCompoundDrawablesRelative(null, null, null, null);
        }
    }

    @DrawableRes
    private static int iconFor(@Nullable String status, Tone tone) {
        String s = normalize(status);
        if (s.equals("DEACTIVATED")) return R.drawable.ic_circle_x;
        if (s.equals("RECEIVED") || s.equals("DELIVERED")) return R.drawable.ic_package;
        switch (tone) {
            case SUCCESS: return R.drawable.ic_check;
            case PROGRESS: return R.drawable.ic_truck;
            case PENDING: return R.drawable.ic_clock;
            case PARTIAL: return R.drawable.ic_package;
            case DANGER: return R.drawable.ic_x;
            case INBOUND: return R.drawable.ic_arrow_down_left;
            case OUTBOUND: return R.drawable.ic_arrow_up_right;
            default: return R.drawable.ic_minus;
        }
    }

    @ColorRes
    public static int textColor(Tone tone) {
        switch (tone) {
            case SUCCESS: return R.color.tone_success_text;
            case PROGRESS: return R.color.tone_progress_text;
            case PENDING:
            case INBOUND: return R.color.tone_pending_text;
            case PARTIAL: return R.color.tone_partial_text;
            case DANGER: return R.color.tone_danger_text;
            case OUTBOUND: return R.color.tone_outbound_text;
            default: return R.color.tone_neutral_text;
        }
    }

    @ColorRes
    public static int bgColor(Tone tone) {
        switch (tone) {
            case SUCCESS: return R.color.tone_success_bg;
            case PROGRESS: return R.color.tone_progress_bg;
            case PENDING:
            case INBOUND: return R.color.tone_pending_bg;
            case PARTIAL: return R.color.tone_partial_bg;
            case DANGER: return R.color.tone_danger_bg;
            case OUTBOUND: return R.color.tone_outbound_bg;
            default: return R.color.tone_neutral_bg;
        }
    }

    @ColorRes
    private static int borderColor(Tone tone) {
        switch (tone) {
            case SUCCESS: return R.color.tone_success_border;
            case PROGRESS: return R.color.tone_progress_border;
            case PENDING:
            case INBOUND: return R.color.tone_pending_border;
            case PARTIAL: return R.color.tone_partial_border;
            case DANGER: return R.color.tone_danger_border;
            case OUTBOUND: return R.color.tone_outbound_border;
            default: return R.color.tone_neutral_border;
        }
    }

    // ------------------------------------------------------------ stock level

    /** Website: LOW_STOCK_THRESHOLD in statusUtils.js */
    public static final int LOW_STOCK_THRESHOLD = 20;

    /**
     * Shows "Out of stock" / "Low stock" on the badge and returns true, or hides it and returns false.
     * Uses the backend's stockWarning text when it sends one.
     */
    public static boolean applyStockBadge(@Nullable TextView badge, int units, @Nullable String stockWarning) {
        if (badge == null) return false;
        String warning = normalize(stockWarning);
        if (units <= 0 || warning.contains("OUT")) {
            badge.setText(R.string.stock_out);
            applyTone(badge, Tone.DANGER, R.drawable.ic_alert_triangle);
            badge.setVisibility(android.view.View.VISIBLE);
            return true;
        }
        if (units <= LOW_STOCK_THRESHOLD || warning.contains("LOW")) {
            badge.setText(R.string.stock_low);
            applyTone(badge, Tone.PENDING, R.drawable.ic_alert_triangle);
            badge.setVisibility(android.view.View.VISIBLE);
            return true;
        }
        badge.setVisibility(android.view.View.GONE);
        return false;
    }

    private static float dp(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }
}
