package com.example.francosperfumemobile.helpers;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date helpers that work on API 24 (no java.time).
 * The backend sends ISO strings like "2026-10-26T10:11:34.269Z" or "2026-10-26T10:11:34".
 * A string without a zone is treated as UTC, which is what the backend stores (DateTime.UtcNow).
 */
public final class DateUtils {

    private DateUtils() {
    }

    @Nullable
    public static Date parse(@Nullable String iso) {
        if (iso == null) return null;
        String s = iso.trim();
        if (s.isEmpty()) return null;

        try {
            if (s.length() == 10) { // yyyy-MM-dd
                SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                day.setTimeZone(TimeZone.getDefault());
                return day.parse(s);
            }

            s = s.replace(' ', 'T');
            // Drop fractional seconds: 10:11:34.2690000 → 10:11:34
            s = s.replaceAll("(T\\d{2}:\\d{2}:\\d{2})\\.\\d+", "$1");

            String zone = "+0000";
            if (s.endsWith("Z") || s.endsWith("z")) {
                s = s.substring(0, s.length() - 1);
            } else if (s.matches(".*T\\d{2}:\\d{2}(:\\d{2})?[+-]\\d{2}:?\\d{2}$")) {
                int idx = Math.max(s.lastIndexOf('+'), s.lastIndexOf('-'));
                zone = s.substring(idx).replace(":", "");
                s = s.substring(0, idx);
            }
            if (s.matches(".*T\\d{2}:\\d{2}$")) s = s + ":00";

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
            return format.parse(s + zone);
        } catch (ParseException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    /** "Oct 26, 2026", or the raw text if it can't be read. */
    public static String formatDate(@Nullable String iso) {
        Date date = parse(iso);
        if (date == null) return iso == null || iso.isEmpty() ? "—" : iso;
        return new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date);
    }

    /** "Oct 26, 2026 · 6:11 PM" */
    public static String formatDateTime(@Nullable String iso) {
        Date date = parse(iso);
        if (date == null) return iso == null || iso.isEmpty() ? "—" : iso;
        return new SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault()).format(date);
    }

    /** "Just now", "5m ago", "3h ago", "Yesterday", or a date. Used in notifications. */
    public static String relative(@Nullable String iso) {
        Date date = parse(iso);
        if (date == null) return iso == null ? "" : iso;
        long diff = System.currentTimeMillis() - date.getTime();
        long minutes = diff / 60000L;
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + "m ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        if (hours < 48) return "Yesterday";
        long days = hours / 24;
        if (days < 7) return days + "d ago";
        return new SimpleDateFormat("MMM d", Locale.getDefault()).format(date);
    }

    /** yyyy-MM-dd for a date the user picked (month is 0-based like Calendar). */
    public static String toIsoDay(int year, int month, int day) {
        return String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
    }

    /**
     * Picked day as an ISO UTC timestamp for the backend, e.g. "2027-01-31T12:00:00Z".
     * Noon (not midnight) so the day stays the same when shown in any time zone.
     */
    public static String toIsoUtcDate(int year, int month, int day) {
        return toIsoDay(year, month, day) + "T12:00:00Z";
    }

    /** yyyy-MM-dd of a date in the phone's time zone. */
    public static String formatIsoDay(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
    }

    /** Calendar for an ISO string, or null. */
    @Nullable
    public static Calendar toCalendar(@Nullable String iso) {
        Date date = parse(iso);
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /** Good morning / afternoon / evening, for the dashboard greeting. */
    public static String greeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) return "Good morning";
        if (hour < 18) return "Good afternoon";
        return "Good evening";
    }
}
