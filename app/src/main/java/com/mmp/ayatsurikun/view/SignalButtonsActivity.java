package com.mmp.ayatsurikun.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.databinding.ActivitySignalButtonsBinding;
import com.mmp.ayatsurikun.viewmodel.SignalButtonsViewModel;

public class SignalButtonsActivity extends AppCompatActivity implements SignalButtonsContract {
    private SignalButtonsViewModel viewModel;
    private AddButtonDialogFragment dialogFragment;
    private ActivitySignalButtonsBinding binding;

    public static void start(Context context) {
        final Intent intent = new Intent(context, SignalButtonsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signal_buttons);
        SignalButtonsViewModel.Factory factory = new SignalButtonsViewModel.Factory(
                ((App)getApplication()).getDevice()
        );
        viewModel = new ViewModelProvider(this, factory).get(SignalButtonsViewModel.class);
        dialogFragment = new AddButtonDialogFragment(viewModel);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getSignal().observe(this, bytes -> {
            viewModel.setSignal(bytes);
            startAddButtonDialog();
        });
        viewModel.connect();
    }

    @Override
    public void onPause() {
        if(dialogFragment.isAdded()) {
            dialogFragment.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        viewModel.disconnect();
        super.onDestroy();
    }

    @Override
    public void startAddButtonDialog() {
        if(dialogFragment.isAdded()) dialogFragment.dismiss();
        dialogFragment.show(getSupportFragmentManager(), "AddButtonDialogFragment");
    }

    @Override
    public void addButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(v -> viewModel.onSignalButtonClick(((Button)v).getText().toString()));
        binding.linearLayout.addView(button);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}