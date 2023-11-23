package com.mmp.ayatsurikun.model.connector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;

import java.nio.ByteBuffer;

public class BluetoothConnector implements DeviceConnector, BluetoothCommunicationThread.Listener {
    private final SignalButtonsContract contract;
    private final String macAddress;
    private final Handler mainLooper;
    private BluetoothConnectThread connectThread;
    private BluetoothCommunicationThread communicationThread;
    private final MutableLiveData<byte[]> signal = new MutableLiveData<>();
    private final BluetoothConnectThread.ConnectedCallback callback = socket -> {
        communicationThread = new BluetoothCommunicationThread(socket, this);
        connectThread.start();
    };
    private byte[] data;

    public BluetoothConnector(SignalButtonsContract contract, String macAddress) {
        this.contract = contract;
        this.macAddress = macAddress;
        mainLooper = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onNewData(byte[] data) {
        mainLooper.post(() -> receive(data));
    }

    @Override
    public void onRunError(Exception e) {
        mainLooper.post(() -> {
            status("connection lost: " + e.getMessage());
            disconnect();
        });
    }

    @Override
    public void setUp() {

    }

    @Override
    public void connect() {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
        if (device == null) {
            Log.e("Connection", "Device not found.");
            return;
        }
        connectThread = new BluetoothConnectThread(device, callback);
        connectThread.start();
    }

    @Override
    public void disconnect() {
        if(connectThread.isConnected()) {
            if(communicationThread != null) {
                communicationThread.setListener(null);
                communicationThread.cancel();
            }
            communicationThread = null;
            if (connectThread.isConnected())connectThread.cancel();
            connectThread = null;
        }
    }

    @Override
    public void send(byte[] signal) {
        if(!connectThread.isConnected()) {
            Toast.makeText((Activity)contract, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            communicationThread.write(signal);
        } catch (Exception e) {
            onRunError(e);
        }
    }

    private void receive(byte[] data) {
        if (this.data == null) this.data = data;
        else {
            ByteBuffer byteBuffer = ByteBuffer.allocate(this.data.length + data.length);
            byteBuffer.put(this.data);
            byteBuffer.put(data);
            this.data = byteBuffer.array();
        }
        if ((char) data[data.length - 1] == '\n') {
            contract.addText("receive:" + new String(this.data));
            signal.postValue(this.data);
            this.data = null;
        }
    }

    @Override
    public LiveData<byte[]> getSignal() {
        return this.signal;
    }

    private void status(String str) {
        contract.addText(str + "\n");
    }
}
