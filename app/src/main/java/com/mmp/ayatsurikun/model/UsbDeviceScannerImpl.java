package com.mmp.ayatsurikun.model;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.FtdiSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.mmp.ayatsurikun.contract.DeviceListViewContract;

import java.util.ArrayList;
import java.util.List;

public class UsbDeviceScannerImpl implements DeviceScanner {
    private int baudRate = 19200;

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
                            Integer.toString(device.getVendorId()),
                            Integer.toString(port),
                            device.getProductName(),
                            driver.getClass().getSimpleName()));
                }
            } else {
                deviceItems.add(new DeviceItem(
                        Integer.toString(device.getVendorId()),
                        "0",
                        device.getProductName(),
                        ""));
            }
        }
        return deviceItems;
    }

    static class CustomProber {
        private static UsbSerialProber getCustomProber() {
            ProbeTable customTable = new ProbeTable();
            customTable.addProduct(0x1234, 0x0001, FtdiSerialDriver.class); // e.g. device with custom VID+PID
            customTable.addProduct(0x1234, 0x0002, FtdiSerialDriver.class); // e.g. device with custom VID+PID
            return new UsbSerialProber(customTable);
        }
    }
}
