package com.mmp.ayatsurikun.model.scanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothDeviceScannerImpl implements DeviceScanner {
    @Override
    public List<DeviceItem> scanDevices(DeviceListViewContract deviceListView) {
        List<DeviceItem> deviceItems = new ArrayList<>();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i("Bluetooth", "Bluetooth is not available!");
            return null;
        }

        if (ActivityCompat.checkSelfPermission((Context) deviceListView, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Bluetooth", "Permission denied!");
            return null;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceItems.add(new DeviceItem(
                        "Bluetooth Connection",
                        0,
                        device.getAddress(),
                        device.getName(),
                        0)
                );
            }
        }
        return deviceItems;
    }
}
