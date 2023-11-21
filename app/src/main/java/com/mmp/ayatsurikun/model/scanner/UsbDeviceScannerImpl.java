package com.mmp.ayatsurikun.model.scanner;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.model.CustomProber;

import java.util.ArrayList;
import java.util.List;

public class UsbDeviceScannerImpl implements DeviceScanner {
    @Override
    public List<DeviceItem> scanDevices(DeviceListViewContract deviceListView) {
        List<DeviceItem> deviceItems = new ArrayList<>();
        UsbManager usbManager = (UsbManager) ((Activity)deviceListView).getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        for(UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if(driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if(driver != null) {
                for(int port = 0; port < driver.getPorts().size(); port++) {
                    deviceItems.add(new DeviceItem(
                            driver.getClass().getSimpleName(),
                            port,
                            device.getProductName(),
                            device.getDeviceName(),
                            device.getDeviceId()));
                }
            } else {
                deviceItems.add(new DeviceItem(
                        "",
                        0,
                        device.getProductName(),
                        device.getDeviceName(),
                        device.getDeviceId()));
            }
        }
        return deviceItems;
    }
}
