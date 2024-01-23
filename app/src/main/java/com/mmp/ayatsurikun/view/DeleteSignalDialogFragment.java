package com.mmp.ayatsurikun.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.DeviceControlViewModel;

public class DeleteSignalDialogFragment extends DialogFragment {
    private final DeviceControlViewModel viewModel;
    private final Signal signal;

    public DeleteSignalDialogFragment(DeviceControlViewModel viewModel, Signal signal) {
        this.viewModel = viewModel;
        this.signal = signal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm);
        builder.setMessage(signal.getName() + getString(R.string.delete_confirm));
        builder.setPositiveButton(R.string.delete, (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                viewModel.deleteSignal(signal);
            }
        });
        builder.setNegativeButton(R.string.dialog_btn_ng, (dialog, which) -> viewModel.clearLongClickedSignal());
        return builder.create();
    }
}
