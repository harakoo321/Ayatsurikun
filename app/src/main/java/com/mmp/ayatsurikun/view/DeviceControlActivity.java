package com.mmp.ayatsurikun.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ActivityDeviceControlBinding;
import com.mmp.ayatsurikun.viewmodel.DeviceControlViewModel;

public class DeviceControlActivity extends AppCompatActivity {
    private DeviceControlViewModel viewModel;
    private AddButtonDialogFragment dialogFragment;

    public static void start(Context context) {
        final Intent intent = new Intent(context, DeviceControlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDeviceControlBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_device_control);
        DeviceControlViewModel.Factory factory = new DeviceControlViewModel.Factory(
                ((App)getApplication()).getDevice()
        );
        viewModel = new ViewModelProvider(this, factory).get(DeviceControlViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.connect();
        viewModel.getSignal().observe(this, bytes -> {
            if(bytes != null) startAddButtonDialog(bytes);
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
        if (dialogFragment != null && dialogFragment.isAdded()) {
            dialogFragment.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        viewModel.disconnect();
        super.onDestroy();
    }

    public void startAddButtonDialog(byte[] signal) {
        if(dialogFragment != null && dialogFragment.isAdded()) dialogFragment.dismiss();
        dialogFragment = new AddButtonDialogFragment(viewModel, signal);
        dialogFragment.show(getSupportFragmentManager(), "AddButtonDialogFragment");
    }
}