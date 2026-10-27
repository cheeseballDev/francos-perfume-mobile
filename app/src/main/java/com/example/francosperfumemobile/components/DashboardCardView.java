package com.example.francosperfumemobile.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.helpers.StatusStyle;

/**
 * KPI tile: title, icon, big number and a short line or badge under it
 * (website: StatusCard on the dashboard).
 */
public class DashboardCardView extends FrameLayout {

    private TextView title, value, warning;
    private ImageView image;
    private View iconTile;

    public DashboardCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs) {
        inflate(context, R.layout.card_dashboard, this);
        image = findViewById(R.id.image_view_dashboard_icon);
        iconTile = findViewById(R.id.layout_dashboard_icon);
        title = findViewById(R.id.text_view_dashboard_title);
        value = findViewById(R.id.text_view_dashboard_value);
        warning = findViewById(R.id.text_view_dashboard_warning);
        warning.setVisibility(GONE);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashboardCardView);
            int icon = a.getResourceId(R.styleable.DashboardCardView_dashboardCardIcon, 0);
            String t = a.getString(R.styleable.DashboardCardView_dashboardCardTitle);
            String v = a.getString(R.styleable.DashboardCardView_dashboardCardValue);
            String w = a.getString(R.styleable.DashboardCardView_dashboardCardWarning);
            a.recycle();
            if (icon != 0) image.setImageResource(icon);
            if (t != null) title.setText(t);
            if (v != null) value.setText(v);
            if (w != null) setSubText(w);
        }
    }

    /** Old API: title, value and a plain line under it. The icon is picked from the title. */
    public void setData(String title, String value, String warning) {
        setIconFromTitle(title);
        if (this.title != null) this.title.setText(title);
        setValue(value);
        setSubText(warning);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setValue(String text) {
        value.setText(text);
        updateDescription();
    }

    /** Coloured icon on a soft tile of the same colour (website: text-emerald-500 etc.). */
    public void setIcon(@DrawableRes int iconRes, @ColorRes int colorRes) {
        int color = ContextCompat.getColor(getContext(), colorRes);
        image.setImageResource(iconRes);
        image.setImageTintList(ColorStateList.valueOf(color));
        iconTile.setBackgroundTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 36)));
    }

    /** Plain grey line under the number. */
    public void setSubText(@Nullable String text) {
        warning.setBackground(null);
        warning.setCompoundDrawablesRelative(null, null, null, null);
        warning.setPadding(0, 0, 0, 0);
        warning.setTextAppearance(R.style.Text_FP_Caption);
        warning.setText(text);
        warning.setVisibility(text == null || text.isEmpty() ? GONE : VISIBLE);
        updateDescription();
    }

    /** Status-style badge under the number, e.g. "12 items in low stock". */
    public void setSubBadge(String text, StatusStyle.Tone tone, @DrawableRes int iconRes) {
        int h = Math.round(getResources().getDisplayMetrics().density * 8);
        int v = Math.round(getResources().getDisplayMetrics().density * 3);
        warning.setTextAppearance(R.style.Text_FP_Caption);
        warning.setTextSize(11);
        warning.setTypeface(warning.getTypeface(), android.graphics.Typeface.BOLD);
        warning.setPadding(h, v, h, v);
        warning.setCompoundDrawablePadding(Math.round(getResources().getDisplayMetrics().density * 4));
        warning.setText(text);
        StatusStyle.applyTone(warning, tone, iconRes);
        warning.setVisibility(VISIBLE);
        updateDescription();
    }

    private void updateDescription() {
        setContentDescription(title.getText() + ": " + value.getText()
                + (warning.getVisibility() == VISIBLE ? ". " + warning.getText() : ""));
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        View card = findViewById(R.id.card_dashboard_root);
        if (card != null) card.setOnClickListener(listener);
        else super.setOnClickListener(listener);
    }

    private boolean containsIgnoreCase(String type, String text) {
        if (type == null || text == null) return false;
        return type.toUpperCase(java.util.Locale.ROOT).contains(text.toUpperCase(java.util.Locale.ROOT));
    }

    private void setIconFromTitle(String type) {
        if (containsIgnoreCase(type, "INVENTORY")) {
            setIcon(R.drawable.ic_boxes, R.color.icon_green);
        } else if (containsIgnoreCase(type, "APPROVAL") || containsIgnoreCase(type, "REQUEST")) {
            setIcon(R.drawable.ic_clock, R.color.icon_blue);
        } else if (containsIgnoreCase(type, "DISPATCH")) {
            setIcon(R.drawable.ic_package, R.color.icon_amber);
        } else if (containsIgnoreCase(type, "DELIVER")) {
            setIcon(R.drawable.ic_truck, R.color.icon_blue);
        } else {
            image.setImageResource(R.drawable.francos_perfume_logo);
            image.setImageTintList(null);
        }
    }
}
