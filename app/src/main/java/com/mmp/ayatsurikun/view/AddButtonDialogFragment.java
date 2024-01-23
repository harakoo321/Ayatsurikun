package com.mmp.ayatsurikun.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.viewmodel.DeviceControlViewModel;

public class AddButtonDialogFragment extends DialogFragment {
    private EditText editText;
    private final DeviceControlViewModel viewModel;
    private final byte[] signal;
    public AddButtonDialogFragment(DeviceControlViewModel viewModel, byte[] signal) {
        this.viewModel = viewModel;
        this.signal = signal;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        editText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_msg);
        builder.setView(editText);
        builder.setPositiveButton(R.string.dialog_btn_ok, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                viewModel.addSignal(editText.getText().toString(), signal);
            }
        });
        builder.setNegativeButton(R.string.dialog_btn_ng, (dialog, which) -> viewModel.cancel());
        return builder.create();
    }
}
