package com.mmp.ayatsurikun.repository;

import com.mmp.ayatsurikun.model.Device;

import java.util.List;

public interface DeviceRepository {
    List<Device> scanDevices();
}