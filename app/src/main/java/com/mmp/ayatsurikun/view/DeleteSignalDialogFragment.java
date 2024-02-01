package com.mmp.ayatsurikun.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.SignalListViewModel;

public class DeleteSignalDialogFragment extends DialogFragment {
    private final SignalListViewModel viewModel;
    private final Signal signal;

    public DeleteSignalDialogFragment(SignalListViewModel viewModel, Signal signal) {
        this.viewModel = viewModel;
        this.signal = signal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.confirm);
        builder.setMessage(signal.getName() + getString(R.string.delete_confirm));
        builder.setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteSignal(signal));
        builder.setNegativeButton(R.string.dialog_btn_ng, (dialog, which) -> viewModel.clearLongClickedSignal());
        return builder.create();
    }
}
