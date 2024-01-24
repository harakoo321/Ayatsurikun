package com.mmp.ayatsurikun.repository;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.util.ConnectionType;
import com.mmp.ayatsurikun.model.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class BluetoothDeviceRepositoryImpl implements DeviceRepository {
    @Inject
    public BluetoothDeviceRepositoryImpl() {}

    @Override
    public List<Device> scanDevices() {
        List<Device> deviceItems = new ArrayList<>();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i("Bluetooth", "Bluetooth is not available!");
            return null;
        }

        if (ActivityCompat.checkSelfPermission(
                App.ContextProvider.getContext(),
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Bluetooth", "Permission denied!");
            return null;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceItems.add(new com.mmp.ayatsurikun.model.BluetoothDevice(
                        device.getAddress(),
                        device.getName(),
                        0,
                        ConnectionType.BLUETOOTH_SPP)
                );
            }
        }
        return deviceItems;
    }
}
