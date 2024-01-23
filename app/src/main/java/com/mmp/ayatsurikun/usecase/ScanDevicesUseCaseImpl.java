package com.mmp.ayatsurikun.usecase;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.repository.BluetoothDeviceRepositoryImpl;
import com.mmp.ayatsurikun.repository.DeviceRepository;
import com.mmp.ayatsurikun.repository.UsbDeviceRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ScanDevicesUseCaseImpl implements ScanDevicesUseCase {
    private final List<DeviceRepository> deviceRepositories = new ArrayList<>();
    public ScanDevicesUseCaseImpl() {
        deviceRepositories.add(new UsbDeviceRepositoryImpl());
        deviceRepositories.add(new BluetoothDeviceRepositoryImpl());
    }

    @Override
    public List<Device> scanAllDevices() {
        List<Device> deviceItems = new ArrayList<>();
        for(DeviceRepository deviceRepository : deviceRepositories) {
            deviceItems.addAll(deviceRepository.scanDevices());
        }
        return deviceItems;
    }
}
