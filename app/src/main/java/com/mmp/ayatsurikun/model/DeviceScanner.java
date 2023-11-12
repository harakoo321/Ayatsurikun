package com.mmp.ayatsurikun.model;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;

import java.util.List;

public interface DeviceScanner {
    List<DeviceItem> scanDevices(DeviceListViewContract deviceListView);

    class DeviceItem {
        public final String vendorId;
        public final String port;
        public final String productName;
        public final String deviceName;


        public DeviceItem(String vendorId, String port, String productName, String deviceName) {
            this.vendorId = vendorId;
            this.port = port;
            this.productName = productName;
            this.deviceName = deviceName;
        }
    }
}