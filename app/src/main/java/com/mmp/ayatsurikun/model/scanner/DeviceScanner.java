package com.mmp.ayatsurikun.model.scanner;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;

import java.util.List;

public interface DeviceScanner {
    List<DeviceItem> scanDevices(DeviceListViewContract deviceListView);
    int getBaudRate();

    class DeviceItem {
        public final String driverName;
        public final int port;
        public final String productName;
        public final String deviceName;
        public final int deviceId;


        public DeviceItem(String driverName, int port, String productName, String deviceName, int deviceId) {
            this.driverName = driverName;
            this.port = port;
            this.productName = productName;
            this.deviceName = deviceName;
            this.deviceId = deviceId;

        }
    }
}