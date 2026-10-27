package com.example.francosperfumemobile.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.francosperfumemobile.R;

/** Shows the message the requester wrote. */
public class MessageDialog extends DialogFragment {

    private static final String ARG_MESSAGE = "MESSAGE_KEY";
    private static final String ARG_TITLE = "TITLE_KEY";

    public static MessageDialog newInstance(@Nullable String message) {
        return newInstance(null, message);
    }

    public static MessageDialog newInstance(@Nullable String title, @Nullable String message) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_FP_Dialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewTitle = view.findViewById(R.id.text_dialog_title);
        TextView textViewMessage = view.findViewById(R.id.text_dialog_content);
        view.findViewById(R.id.button_icon_close).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.button_close).setOnClickListener(v -> dismiss());

        Bundle args = getArguments();
        String title = args != null ? args.getString(ARG_TITLE) : null;
        String message = args != null ? args.getString(ARG_MESSAGE) : null;
        if (title != null) textViewTitle.setText(title);
        boolean empty = message == null || message.trim().isEmpty() || "N/A".equalsIgnoreCase(message.trim());
        textViewMessage.setText(empty ? getString(R.string.no_message_text) : message.trim());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
