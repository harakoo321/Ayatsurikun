package com.mmp.ayatsurikun.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.util.ConnectionType;

import java.nio.ByteBuffer;

public class BluetoothDevice implements Device, BluetoothCommunicationThread.Listener {
    private static final String TAG = BluetoothDevice.class.getSimpleName();
    private final String id;
    private final String name;
    private final int port;
    private final ConnectionType connectionType;
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
            mainLooper.post(() -> showToast(R.string.connected));
        }

        @Override
        public void connectionFailed() {
            mainLooper.post(() -> showToast(R.string.connection_failed));
        }
    };

    public BluetoothDevice(String id, String name, int port, ConnectionType connectionType) {
        this.id = id;
        this.name = name;
        this.port = port;
        this.connectionType = connectionType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    public LiveData<byte[]> getSignal() {
        return this.signal;
    }

    @Override
    public void clearSignal() {
        signal.postValue(null);
    }

    @Override
    public void connect() {
        android.bluetooth.BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(id);
        if (device == null) {
            showToast(R.string.not_found);
            return;
        }
        connectThread = new BluetoothConnectThread(device, callback);
        connectThread.start();
        try {
            connectThread.join();
        } catch (InterruptedException e) {
            showToast(R.string.connection_failed);
        }
    }

    @Override
    public void disconnect() {
        if (connectThread == null) {
            showToast(R.string.not_connected);
            return;
        }
        if(connectThread.isConnected()) {
            communicationThread.cancel();
            connectThread.cancel();
            showToast(R.string.disconnected);

        }
    }

    @Override
    public void send(byte[] signal) {
        if(connectThread == null || !connectThread.isConnected()) {
            showToast(R.string.not_connected);
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
            Log.i(TAG, "received");
            signal.postValue(this.data);
            this.data = null;
        }
    }

    @Override
    public void onNewData(byte[] data) {
        mainLooper.post(() -> receive(data));
    }

    @Override
    public void onRunError(Exception e) {
        mainLooper.post(this::disconnect);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BluetoothDevice)) return false;
        BluetoothDevice that = (BluetoothDevice) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                port == that.port &&
                connectionType == that.connectionType;
    }

    private void showToast(int resId) {
        Toast.makeText(App.ContextProvider.getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
