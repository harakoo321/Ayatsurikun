package com.mmp.ayatsurikun.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.databinding.ActivityMainBinding;
import com.mmp.ayatsurikun.model.scanner.DeviceScanner;
import com.mmp.ayatsurikun.model.scanner.UsbDeviceScannerImpl;
import com.mmp.ayatsurikun.viewmodel.DeviceListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceListViewContract {

    private DeviceAdapter deviceAdapter;
    private DeviceListViewModel deviceListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        final DeviceScanner deviceScanner = new UsbDeviceScannerImpl();
        deviceListViewModel = new DeviceListViewModel( this, deviceScanner);
        binding.setViewModel(deviceListViewModel);
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceListViewModel.loadDevices();
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
        SignalButtonsActivity.start(this, deviceId, port, deviceListViewModel.getBaudRate());
    }
}