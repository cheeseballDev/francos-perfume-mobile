package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.example.francosperfumemobile.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec;
import com.google.android.material.progressindicator.IndeterminateDrawable;

/**
 * Shows a small spinner inside a button and disables it while a call runs, like the
 * website's buttons ("Sending code..." with a spinning Loader2). Also stops double taps.
 */
public final class ButtonLoading {

    private static final int TAG_IDLE_TEXT = R.id.tag_button_idle_text;
    private static final int TAG_IDLE_ICON = R.id.tag_button_idle_icon;

    private ButtonLoading() {
    }

    public static void start(@Nullable MaterialButton button, @Nullable CharSequence loadingText) {
        if (button == null) return;
        if (button.getTag(TAG_IDLE_TEXT) == null) {
            button.setTag(TAG_IDLE_TEXT, button.getText());
            button.setTag(TAG_IDLE_ICON, button.getIcon() == null ? "none" : button.getIcon());
        }
        Context context = button.getContext();
        CircularProgressIndicatorSpec spec = new CircularProgressIndicatorSpec(context, null, 0, R.style.Widget_FP_Progress_Small);
        spec.indicatorSize = dp(context, 18);
        spec.trackThickness = dp(context, 2);
        spec.indicatorColors = new int[]{button.getCurrentTextColor()};
        IndeterminateDrawable<CircularProgressIndicatorSpec> spinner = IndeterminateDrawable.createCircularDrawable(context, spec);

        button.setIcon(spinner);
        button.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
        if (loadingText != null) button.setText(loadingText);
        button.setEnabled(false);
    }

    public static void stop(@Nullable MaterialButton button) {
        if (button == null) return;
        Object idleText = button.getTag(TAG_IDLE_TEXT);
        Object idleIcon = button.getTag(TAG_IDLE_ICON);
        if (idleText instanceof CharSequence) button.setText((CharSequence) idleText);
        button.setIcon(idleIcon instanceof android.graphics.drawable.Drawable ? (android.graphics.drawable.Drawable) idleIcon : null);
        button.setTag(TAG_IDLE_TEXT, null);
        button.setTag(TAG_IDLE_ICON, null);
        button.setEnabled(true);
    }

    public static boolean isLoading(@Nullable MaterialButton button) {
        return button != null && button.getTag(TAG_IDLE_TEXT) != null;
    }

    private static int dp(Context context, float value) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }
}
