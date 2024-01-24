package com.mmp.ayatsurikun.usecase;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.repository.DeviceRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class ScanDevicesUseCaseImpl implements ScanDevicesUseCase {
    private final List<DeviceRepository> deviceRepositories = new ArrayList<>();
    @Inject
    public ScanDevicesUseCaseImpl(
            @Named("Usb") DeviceRepository usbDeviceRepository,
            @Named("Bluetooth") DeviceRepository bluetoothDeviceRepository) {
        deviceRepositories.add(usbDeviceRepository);
        deviceRepositories.add(bluetoothDeviceRepository);
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
