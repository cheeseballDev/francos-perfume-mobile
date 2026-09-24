package com.example.francosperfumemobile.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.BundleCompat;
import androidx.fragment.app.DialogFragment;

import com.example.francosperfumemobile.R;
import com.example.francosperfumemobile.backend.dtos.inventorydtos.DisplayBatchDTO;

public class EditBatchDialog extends DialogFragment {

    TextView textViewSelectedBatch, textViewSelectedProduct, textViewDateReceived;
    EditText editTextTargetDate, editTextBatchStock;
    Spinner dropdownReason;
    ImageButton buttonPlus, buttonMinus;


    public static EditBatchDialog newInstance(DisplayBatchDTO selectedBatch) {
        EditBatchDialog dialog = new EditBatchDialog();
        Bundle args = new Bundle();
        args.putParcelable("BATCH_ITEM", selectedBatch);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_edit_batch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            DisplayBatchDTO item = BundleCompat.getParcelable(getArguments(), "BATCH_ITEM", DisplayBatchDTO.class);
            if (item != null) {
                textViewSelectedBatch.setText(String.format("Edit Batch: %s", item.getBatchDisplayId()));
                textViewSelectedProduct.setText(item.getProductName());
                //TODO: Add getDateReceived in DisplayBatchDTO
                //textViewDateReceived.setText(item.getDateReceived());
                editTextTargetDate.setText(item.getExpiryDate());
                editTextBatchStock.setText(String.valueOf(item.getQuantity()));
            }
        }
    }
}
