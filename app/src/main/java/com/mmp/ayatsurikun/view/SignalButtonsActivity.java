package com.mmp.ayatsurikun.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.databinding.ActivitySignalButtonsBinding;
import com.mmp.ayatsurikun.viewmodel.SignalButtonsViewModel;

public class SignalButtonsActivity extends AppCompatActivity implements SignalButtonsContract {
    private static final String EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID";
    private static final String EXTRA_PORT = "EXTRA_PORT";
    private static final String EXTRA_BAUD_RATE = "EXTRA_BAUD_RATE";
    private SignalButtonsViewModel viewModel;

    private ActivitySignalButtonsBinding binding;

    public static void start(Context context, int deviceId, int portNum, int baudRate) {
        final Intent intent = new Intent(context, SignalButtonsActivity.class);
        intent.putExtra(EXTRA_DEVICE_ID, deviceId);
        intent.putExtra(EXTRA_PORT, portNum);
        intent.putExtra(EXTRA_BAUD_RATE, baudRate);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signal_buttons);
        final Intent intent = getIntent();
        viewModel = new SignalButtonsViewModel(
                this,
                intent.getIntExtra(EXTRA_DEVICE_ID, 0),
                intent.getIntExtra(EXTRA_PORT, 0),
                intent.getIntExtra(EXTRA_BAUD_RATE, 0));
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        Log.i("Selected", "" + intent.getIntExtra(EXTRA_DEVICE_ID, 0));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.setUp();
    }

    @Override
    public String getEditedText() {
        return binding.sendText.getText().toString();
    }

    @Override
    public void addText(String str) {
        binding.receiveText.append(str);
    }
}