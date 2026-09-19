package com.example.francosperfumemobile.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.francosperfumemobile.R;

public class DashboardCardView extends FrameLayout {

    private TextView textViewTitle, textViewValue, textViewWarning;

    private ImageView image;

    public DashboardCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.card_dashboard, this);
        image = findViewById(R.id.image_view_dashboard_icon);
        textViewTitle = findViewById(R.id.text_view_dashboard_title);
        textViewValue = findViewById(R.id.text_view_dashboard_value);
        textViewWarning = findViewById(R.id.text_view_dashboard_warning);
    }

    public void setData(String title, String value, String warning) {
        setIcon(title);
        if (textViewTitle != null) textViewTitle.setText(title);
        if (textViewValue != null) textViewValue.setText(value);
        if (textViewWarning != null) textViewWarning.setText(warning);
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