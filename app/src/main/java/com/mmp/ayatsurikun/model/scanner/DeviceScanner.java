package com.mmp.ayatsurikun.model.scanner;

import com.mmp.ayatsurikun.model.Device;

import java.util.List;

public interface DeviceScanner {
    List<Device> scanDevices();
}