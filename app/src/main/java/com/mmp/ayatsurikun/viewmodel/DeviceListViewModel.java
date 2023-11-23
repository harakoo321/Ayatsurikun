package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.model.scanner.BluetoothDeviceScannerImpl;
import com.mmp.ayatsurikun.model.scanner.DeviceScanner;
import com.mmp.ayatsurikun.model.scanner.UsbDeviceScannerImpl;

import java.util.ArrayList;
import java.util.List;

public class DeviceListViewModel extends ViewModel {
    private final DeviceListViewContract deviceListView;
    private final List<DeviceScanner> deviceScanners = new ArrayList<>();

    public DeviceListViewModel(DeviceListViewContract deviceListView) {
        this.deviceListView = deviceListView;
        deviceScanners.add(new UsbDeviceScannerImpl());
        deviceScanners.add(new BluetoothDeviceScannerImpl());
    }
    public void loadDevices() {
        List<DeviceScanner.DeviceItem> deviceItems = new ArrayList<>();
        for (DeviceScanner deviceScanner : deviceScanners) {
            List<DeviceScanner.DeviceItem> items = deviceScanner.scanDevices(deviceListView);
            if (items != null) deviceItems.addAll(items);
        }
        deviceListView.showDevices(deviceItems);
    }
}
