package com.mmp.ayatsurikun.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ActivityDeviceControlBinding;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.DeviceControlViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DeviceControlActivity extends AppCompatActivity {
    private DeviceControlViewModel viewModel;
    private AddSignalDialogFragment addDialogFragment;
    private DeleteSignalDialogFragment deleteDialogFragment;

    public static void start(Context context) {
        final Intent intent = new Intent(context, DeviceControlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDeviceControlBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_device_control);
        viewModel = new ViewModelProvider(this).get(DeviceControlViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.connect();
        viewModel.getSignal().observe(this, bytes -> {
            if(bytes != null) startAddSignalDialog(bytes);
        });
        viewModel.getLongClickedSignal().observe(this, signal -> {
            if(signal != null) startDeleteSignalDialog(signal);
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_signal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SignalAdapter signalAdapter = new SignalAdapter(viewModel);
        recyclerView.setAdapter(signalAdapter);
        viewModel.getAllSignals().observe(this, signalAdapter::submitList);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAllSignals();
    }

    @Override
    public void onPause() {
        if (addDialogFragment != null && addDialogFragment.isAdded()) {
            addDialogFragment.dismiss();
        }
        if (deleteDialogFragment != null && deleteDialogFragment.isAdded()) {
            deleteDialogFragment.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        viewModel.disconnect();
        super.onDestroy();
    }

    public void startAddSignalDialog(byte[] signal) {
        if(addDialogFragment != null && addDialogFragment.isAdded()) addDialogFragment.dismiss();
        addDialogFragment = new AddSignalDialogFragment(viewModel, signal);
        addDialogFragment.show(getSupportFragmentManager(), "AddButtonDialogFragment");
    }

    public void startDeleteSignalDialog(Signal signal) {
        if(deleteDialogFragment != null && deleteDialogFragment.isAdded()) deleteDialogFragment.dismiss();
        deleteDialogFragment = new DeleteSignalDialogFragment(viewModel, signal);
        deleteDialogFragment.show(getSupportFragmentManager(), "DeleteSignalDialogFragment");
    }
}