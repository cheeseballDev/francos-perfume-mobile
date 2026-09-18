package com.example.francosperfumemobile.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.francosperfumemobile.R;

public class DashboardCardView extends FrameLayout {

    private TextView textTitle, textValue, textWarning;

    private ImageView image;

    public DashboardCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.card_dashboard, this);
        image = findViewById(R.id.card_dashboard_icon);
        textTitle = findViewById(R.id.card_dashboard_title);
        textValue = findViewById(R.id.card_dashboard_value);
        textWarning = findViewById(R.id.card_dashboard_warning);
    }

    public void setData(String title, String value, String warning) {
        setIcon(title);
        if (textTitle != null) textTitle.setText(title);
        if (textValue != null) textValue.setText(value);
        if (textWarning != null) textWarning.setText(warning);
    }

    private boolean containsIgnoreCase(String type, String text) {
        if (type == null || text == null) return false;
        return type.toUpperCase(java.util.Locale.ROOT).contains(text.toUpperCase(java.util.Locale.ROOT));
    }

    private void setIcon(String type) {
        if (containsIgnoreCase(type, "TOTAL INVENTORY")) {
            image.setImageResource(R.drawable.icon_inventory);
            applyTint(R.color.green);
        } else if (containsIgnoreCase(type, "PENDING REQUESTS")) {
            image.setImageResource(R.drawable.icon_pending);
            applyTint(R.color.yellow_secondary);
        } else if (containsIgnoreCase(type, "FOR DISPATCH")) {
            image.setImageResource(R.drawable.icon_dispatch);
            applyTint(R.color.blue);
        } else if (containsIgnoreCase(type, "DELIVERIES")) {
            image.setImageResource(R.drawable.icon_delivery);
            applyTint(R.color.blue);
        } else {
            image.setImageResource(R.drawable.francos_perfume_logo);
            image.setImageTintList(null); // Clears tint so fallback logo retains natural colors
        }
    }

    private void applyTint(int colorResId) {
        int resolvedColor = ContextCompat.getColor(getContext(), colorResId);
        image.setImageTintList(ColorStateList.valueOf(resolvedColor));
    }


}