package com.mmp.ayatsurikun.repository;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.util.ConnectionType;
import com.mmp.ayatsurikun.util.CustomProber;
import com.mmp.ayatsurikun.model.Device;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UsbDeviceRepositoryImpl implements DeviceRepository {
    @Inject
    public UsbDeviceRepositoryImpl() {}

    @Override
    public List<Device> scanDevices() {
        List<Device> deviceItems = new ArrayList<>();
        UsbManager usbManager =
                (UsbManager) App.ContextProvider.getContext().getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        for(UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if(driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if(driver != null) {
                for(int port = 0; port < driver.getPorts().size(); port++) {
                    deviceItems.add(new com.mmp.ayatsurikun.model.UsbDevice(
                            device.getDeviceName(),
                            device.getProductName(),
                            port,
                            ConnectionType.USB_SERIAL)
                    );
                }
            } else {
                deviceItems.add(new com.mmp.ayatsurikun.model.UsbDevice(
                        device.getDeviceName(),
                        device.getProductName(),
                        0,
                        ConnectionType.USB_SERIAL)
                );
            }
        }
        return deviceItems;
    }
}
