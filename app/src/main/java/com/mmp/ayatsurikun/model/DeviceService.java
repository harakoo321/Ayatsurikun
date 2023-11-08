package com.mmp.ayatsurikun.model;

import java.util.ArrayList;
import java.util.List;

public interface DeviceService {
    public static final List<DeviceItem> ITEMS = new ArrayList<DeviceItem>();

    static void addItem(DeviceItem item) {
        ITEMS.add(item);
    }

    static DeviceItem createPlaceholderItem(int position) {
        return new DeviceItem(String.valueOf(position), "Item " + position, "");
    }

    public static class DeviceItem {
        public final String id;
        public final String content;
        public final String details;

        public DeviceItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }
    }
}