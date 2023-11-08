package com.mmp.ayatsurikun.model;

import java.util.ArrayList;
import java.util.List;

public interface DeviceService {
    public static final List<DeviceItem> ITEMS = new ArrayList<DeviceItem>();

    static void addItem(DeviceItem item) {
        ITEMS.add(item);
    }

    public static class DeviceItem {
        public final String vendorId;
        public final String productId;
        public final String deviceName;

        public DeviceItem(String vendorId, String productId, String deviceName) {
            this.vendorId = vendorId;
            this.productId = productId;
            this.deviceName = deviceName;
        }
    }
}