package com.example.francosperfumemobile.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.helpers.PasswordRules;

import java.util.List;

/**
 * Live password checklist (website: PasswordConditionsList).
 * Each rule turns green with a check when it is met.
 *
 * <pre>
 *   conditionsView.update(PasswordRules.evaluate(pw, first, last, oldPwOrNull), confirmTyped, matches);
 * </pre>
 */
public class PasswordConditionsView extends LinearLayout {

    private LinearLayout rowsContainer;

    public PasswordConditionsView(Context context) {
        super(context);
        init(context);
    }

    public PasswordConditionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PasswordConditionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_password_conditions, this, true);
        rowsContainer = findViewById(R.id.layout_password_condition_rows);
        update(PasswordRules.evaluate("", null, null, null), false, false);
    }

    /**
     * @param showMatchRow add "Passwords match" (once the user starts typing the confirmation)
     */
    public void update(List<PasswordRules.Condition> conditions, boolean showMatchRow, boolean passwordsMatch) {
        rowsContainer.removeAllViews();
        for (PasswordRules.Condition condition : conditions) {
            addRow(getContext().getString(condition.label), condition.met);
        }
        if (showMatchRow) {
            addRow(getContext().getString(R.string.pw_rule_match), passwordsMatch);
        }
    }

    private void addRow(String text, boolean met) {
        View row = LayoutInflater.from(getContext()).inflate(R.layout.item_password_condition, rowsContainer, false);
        ImageView icon = row.findViewById(R.id.image_view_condition_icon);
        TextView label = row.findViewById(R.id.text_view_condition_label);

        int color = ContextCompat.getColor(getContext(), met ? R.color.tone_success_text : R.color.fp_muted_foreground);
        icon.setImageResource(met ? R.drawable.ic_circle_check : R.drawable.ic_circle_outline);
        icon.setImageTintList(ColorStateList.valueOf(color));
        label.setText(text);
        label.setTextColor(color);
        row.setContentDescription((met ? "Done: " : "Not yet: ") + text);
        rowsContainer.addView(row);
    }
}
