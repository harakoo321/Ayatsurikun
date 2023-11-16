package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.model.scanner.DeviceScanner;

import java.util.List;

public class DeviceListViewModel extends ViewModel {
    private final DeviceListViewContract deviceListView;
    private final DeviceScanner deviceScanner;

    public DeviceListViewModel(DeviceListViewContract deviceListView, DeviceScanner deviceScanner) {
        this.deviceListView = deviceListView;
        this.deviceScanner = deviceScanner;
    }
    public void loadDevices() {
        List<DeviceScanner.DeviceItem> deviceItems = deviceScanner.scanDevices(deviceListView);
        deviceListView.showDevices(deviceItems);
    }

    public int getBaudRate() {
        return deviceScanner.getBaudRate();
    }
}
