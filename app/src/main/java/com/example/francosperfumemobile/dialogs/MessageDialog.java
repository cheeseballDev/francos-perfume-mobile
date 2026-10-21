package com.example.francosperfumemobile.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.francosperfumemobile.R;
import com.google.android.material.button.MaterialButton;

public class MessageDialog extends DialogFragment {
    private TextView textViewMessage;
    private MaterialButton buttonClose;
    private ImageView buttonX;

    public static MessageDialog newInstance(String message) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString("MESSAGE_KEY", message);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewMessage = view.findViewById(R.id.text_dialog_content);
        buttonX = view.findViewById(R.id.button_icon_close);
        buttonClose = view.findViewById(R.id.button_close);

        buttonX.setOnClickListener(v -> {dismiss();});
        buttonClose.setOnClickListener(v -> {dismiss();});


        if (getArguments() != null) {
            textViewMessage.setText(getArguments().getString("MESSAGE_KEY"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}