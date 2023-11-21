package com.mmp.ayatsurikun.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.databinding.ActivityMainBinding;
import com.mmp.ayatsurikun.model.scanner.DeviceScanner;
import com.mmp.ayatsurikun.viewmodel.DeviceListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceListViewContract {

    private DeviceAdapter deviceAdapter;
    private DeviceListViewModel deviceListViewModel;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        deviceListViewModel = new DeviceListViewModel(this);
        binding.setViewModel(deviceListViewModel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission();
        }
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceListViewModel.loadDevices();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
        }
    }

    private void setupViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_dev);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceAdapter = new DeviceAdapter(this, this);
        recyclerView.setAdapter(deviceAdapter);
    }

    @Override
    public void showDevices(List<DeviceScanner.DeviceItem> deviceItems) {
        deviceAdapter.setItemsAndRefresh(deviceItems);
    }

    @Override
    public void startSignalButtonsActivity(int deviceId, int port) {
        SignalButtonsActivity.start(this, deviceId, port);
    }
}