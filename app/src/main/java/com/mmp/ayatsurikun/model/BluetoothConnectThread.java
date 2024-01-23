package com.mmp.ayatsurikun.model;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mmp.ayatsurikun.App;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectThread extends Thread {
    private static final String TAG = BluetoothConnectThread.class.getSimpleName();
    private static final UUID BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothSocket mmSocket;
    private final ConnectedCallback connectedCallback;

    public interface ConnectedCallback {
        void connected(BluetoothSocket socket);

        void connectionFailed();
    }

    public BluetoothConnectThread(BluetoothDevice device, ConnectedCallback connectedCallback) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        this.connectedCallback = connectedCallback;

        try {
            checkPermission();
            tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            checkPermission();
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            connectedCallback.connectionFailed();
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        connectedCallback.connected(mmSocket);
    }

    public boolean isConnected() {
        if (mmSocket == null) return false;
        return mmSocket.isConnected();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

    private void checkPermission() throws IOException {
        if (ActivityCompat.checkSelfPermission(App.ContextProvider.getContext(), Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            throw new IOException("Permission denied");
        }
    }
}
