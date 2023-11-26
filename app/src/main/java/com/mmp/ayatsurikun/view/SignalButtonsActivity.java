package com.mmp.ayatsurikun.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.databinding.ActivitySignalButtonsBinding;
import com.mmp.ayatsurikun.viewmodel.SignalButtonsViewModel;

public class SignalButtonsActivity extends AppCompatActivity implements SignalButtonsContract {
    private static final String EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID";
    private static final String EXTRA_PORT = "EXTRA_PORT";
    private static final String EXTRA_CONNECTION_METHOD = "EXTRA_CONNECTION_METHOD";
    private SignalButtonsViewModel viewModel;
    private AddButtonDialogFragment dialogFragment;

    private ActivitySignalButtonsBinding binding;

    public static void start(Context context, String deviceId, int portNum, String connectionMethod) {
        final Intent intent = new Intent(context, SignalButtonsActivity.class);
        intent.putExtra(EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(EXTRA_PORT, portNum);
        intent.putExtra(EXTRA_CONNECTION_METHOD, connectionMethod);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signal_buttons);
        final Intent intent = getIntent();
        SignalButtonsViewModel.Factory factory = new SignalButtonsViewModel.Factory(
                intent.getStringExtra(EXTRA_DEVICE_ID),
                intent.getIntExtra(EXTRA_PORT, 0),
                115200,
                intent.getStringExtra(EXTRA_CONNECTION_METHOD) + ""
        );
        viewModel = new ViewModelProvider(this, factory).get(SignalButtonsViewModel.class);
        dialogFragment = new AddButtonDialogFragment(viewModel);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getSignal().observe(this, bytes -> {
            viewModel.setSignal(bytes);
            startAddButtonDialog();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.setUp(this);
    }

    @Override
    public void onPause() {
        dialogFragment.dismiss();
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