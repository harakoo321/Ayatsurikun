package com.mmp.ayatsurikun.repository;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mmp.ayatsurikun.util.ConnectionType;
import com.mmp.ayatsurikun.model.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class BluetoothDeviceRepositoryImpl implements DeviceRepository {
    private final Context context;
    @Inject
    public BluetoothDeviceRepositoryImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public List<Device> scanDevices() {
        List<Device> deviceItems = new ArrayList<>();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i("Bluetooth", "Bluetooth is not available!");
            return null;
        }

        if (!checkPermission()) {
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

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;
        }
    }
}
