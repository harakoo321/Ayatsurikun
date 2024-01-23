package com.mmp.ayatsurikun.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ActivityDeviceListBinding;
import com.mmp.ayatsurikun.viewmodel.DeviceListViewModel;

//@AndroidEntryPoint
public class DeviceListActivity extends AppCompatActivity {

    private DeviceListViewModel deviceListViewModel;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDeviceListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_device_list);
        DeviceListViewModel.Factory factory = new DeviceListViewModel.Factory();
        deviceListViewModel = new ViewModelProvider(this, factory).get(DeviceListViewModel.class);
        binding.setViewModel(deviceListViewModel);
        checkPermission();
        setupViews();
        deviceListViewModel.clearSelectedDevice();
        deviceListViewModel.getSelectedDevice().observe(this, device -> {
            ((App)getApplication()).setDevice(device);
            DeviceControlActivity.start(this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceListViewModel.loadDevices();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(App.ContextProvider.getContext(), Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
            else requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH);
        }
    }

    private void setupViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_dev);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DeviceAdapter deviceAdapter = new DeviceAdapter(deviceListViewModel);
        recyclerView.setAdapter(deviceAdapter);
        deviceListViewModel.getDevices().observe(this, deviceAdapter::submitList);
    }
}