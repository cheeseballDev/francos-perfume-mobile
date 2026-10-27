package com.example.francosperfumemobile.helpers;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.francosperfumemobile.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Confirmation dialogs, styled like the website's ConfirmDialog.
 */
public final class DialogHelper {

    public interface OnText {
        void onText(String text);
    }

    private DialogHelper() {
    }

    public static void confirm(Context context, String title, String message, String confirmText,
                               boolean destructive, Runnable onConfirm) {
        int theme = destructive ? R.style.ThemeOverlay_FP_AlertDialog_Destructive : R.style.ThemeOverlay_FP_AlertDialog;
        new MaterialAlertDialogBuilder(context, theme)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel_text, (d, w) -> d.dismiss())
                .setPositiveButton(confirmText, (d, w) -> onConfirm.run())
                .show();
    }

    /**
     * Asks for a short text (e.g. reject remarks). The confirm button stays disabled until
     * something is typed when {@code required} is true.
     */
    public static void prompt(Context context, String title, String message, String hint,
                              @Nullable String prefill, String confirmText, boolean destructive,
                              boolean required, OnText onConfirm) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_text_input, null, false);
        TextInputLayout layout = view.findViewById(R.id.text_input_layout_dialog);
        TextInputEditText input = view.findViewById(R.id.edit_text_dialog);
        layout.setHint(hint);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        if (prefill != null) input.setText(prefill);

        int theme = destructive ? R.style.ThemeOverlay_FP_AlertDialog_Destructive : R.style.ThemeOverlay_FP_AlertDialog;
        AlertDialog dialog = new MaterialAlertDialogBuilder(context, theme)
                .setTitle(title)
                .setMessage(message)
                .setView(view)
                .setNegativeButton(R.string.cancel_text, (d, w) -> d.dismiss())
                .setPositiveButton(confirmText, null) // set below so we can validate first
                .create();

        dialog.setOnShowListener(d -> {
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setEnabled(!required || textOf(input).length() > 0);
            input.addTextChangedListener(new DebouncedTextWatcher.Simple() {
                @Override
                public void afterTextChanged(Editable s) {
                    positive.setEnabled(!required || s.toString().trim().length() > 0);
                    layout.setError(null);
                }
            });
            positive.setOnClickListener(v -> {
                String text = textOf(input);
                if (required && text.isEmpty()) {
                    layout.setError(context.getString(R.string.error_required));
                    return;
                }
                dialog.dismiss();
                onConfirm.onText(text);
            });
            input.requestFocus();
        });
        dialog.show();
    }

    private static String textOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
