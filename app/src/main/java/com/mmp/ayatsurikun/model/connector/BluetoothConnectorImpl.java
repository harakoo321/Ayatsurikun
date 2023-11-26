package com.mmp.ayatsurikun.model.connector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;

import java.nio.ByteBuffer;

public class BluetoothConnectorImpl implements DeviceConnector, BluetoothCommunicationThread.Listener {
    private SignalButtonsContract contract;
    private final String macAddress;
    private final Handler mainLooper = new Handler(Looper.getMainLooper());
    private BluetoothConnectThread connectThread;
    private BluetoothCommunicationThread communicationThread;
    private final MutableLiveData<byte[]> signal = new MutableLiveData<>();
    private byte[] data;
    private final BluetoothCommunicationThread.Listener listener = this;
    private final BluetoothConnectThread.ConnectedCallback callback = new BluetoothConnectThread.ConnectedCallback() {
        @Override
        public void connected(BluetoothSocket socket) {
            communicationThread = new BluetoothCommunicationThread(socket, listener);
            communicationThread.start();
            mainLooper.post(() -> status("connected"));
        }

        @Override
        public void connectionFailed() {
            mainLooper.post(() -> status("connection failed"));
        }
    };

    public BluetoothConnectorImpl(String macAddress) {
        this.macAddress = macAddress;
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
    public void setUp(SignalButtonsContract contract) {
        this.contract = contract;
        mainLooper.post(this::connect);
    }

    @Override
    public void connect() {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
        if (device == null) {
            status("device not found.");
            return;
        }
        connectThread = new BluetoothConnectThread(device, callback);
        connectThread.start();
    }

    @Override
    public void disconnect() {
        if (connectThread == null) return;
        if(connectThread.isConnected()) {
            if(communicationThread != null) {
                communicationThread.setListener(null);
                communicationThread.cancel();
            }
            communicationThread = null;
            if (connectThread.isConnected())connectThread.cancel();
            connectThread = null;
            status("disconnected");
        }
    }

    @Override
    public void send(byte[] signal) {
        if(connectThread == null) {
            status("not connected");
            return;
        }
        if(!connectThread.isConnected()) {
            status("not connected");
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
            status("received");
            signal.postValue(this.data);
            this.data = null;
        }
    }

    @Override
    public LiveData<byte[]> getSignal() {
        return this.signal;
    }

    private void status(String str) {
        contract.showToast(str);
    }
}
