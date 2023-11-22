package com.mmp.ayatsurikun.model.connector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BluetoothConnector implements DeviceConnector {
    private final String macAddress;
    private final MutableLiveData<byte[]> signal = new MutableLiveData<>();
    public BluetoothConnector(String macAddress) {
        this.macAddress = macAddress;
    }
    public void setUp() {

    }
    public void connect() {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
        if (device == null) {
            Log.e("Connection", "Device not found.");
            return;
        }
    }
    public void disconnect() {

    }
    public void send(byte[] signal) {

    }
    public LiveData<byte[]> getSignal() {
        return this.signal;
    }
}
