package com.mmp.ayatsurikun.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.viewmodel.SignalButtonsViewModel;

public class AddButtonDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private EditText editText;
    private final SignalButtonsViewModel viewModel;
    public AddButtonDialogFragment(SignalButtonsViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        editText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_msg);
        builder.setView(editText);
        builder.setPositiveButton(R.string.dialog_btn_ok, this);
        builder.setNegativeButton(R.string.dialog_btn_ng, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            viewModel.setButtonText(editText.getText().toString(), (SignalButtonsContract)getActivity());
        }
    }
}
