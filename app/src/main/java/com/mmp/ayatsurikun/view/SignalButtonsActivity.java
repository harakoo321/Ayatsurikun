package com.mmp.ayatsurikun.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ActivitySignalButtonsBinding;
import com.mmp.ayatsurikun.viewmodel.SignalButtonsViewModel;

public class SignalButtonsActivity extends AppCompatActivity {
    private static final String EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME";

    public static void start(Context context, String deviceName) {
        final Intent intent = new Intent(context, SignalButtonsActivity.class);
        intent.putExtra(EXTRA_DEVICE_NAME, deviceName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivitySignalButtonsBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_signal_buttons);
        final SignalButtonsViewModel signalButtonsViewModel = new SignalButtonsViewModel();
        binding.setViewModel(signalButtonsViewModel);

        final Intent intent = getIntent();
        Log.i("Selected", "" + intent.getStringExtra(EXTRA_DEVICE_NAME));
    }
}