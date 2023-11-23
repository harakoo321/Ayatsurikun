package com.mmp.ayatsurikun.contract;

import com.mmp.ayatsurikun.model.scanner.DeviceScanner;

import java.util.List;

public interface DeviceListViewContract {
    void showDevices(List<DeviceScanner.DeviceItem> deviceItems);
    void startSignalButtonsActivity(String deviceId, int port, String connectionMethod);
}
