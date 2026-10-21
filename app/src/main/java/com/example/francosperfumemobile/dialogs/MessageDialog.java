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

public class MessageDialog extends DialogFragment {
    private TextView textViewMessage;

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
        if (getArguments() != null) {
            textViewMessage.setText(getArguments().getString("MESSAGE_KEY"));
        }
    }
}