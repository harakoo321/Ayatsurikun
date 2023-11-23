package com.mmp.ayatsurikun.model.connector;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothCommunicationThread extends Thread {
    private static final String TAG = BluetoothCommunicationThread.class.getSimpleName();
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Listener listener;

    public interface Listener {
        void onNewData(byte[] data);
        void onRunError(Exception e);
    }

    public BluetoothCommunicationThread(BluetoothSocket socket, Listener listener) {
        mmSocket = socket;
        this.listener = listener;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void run() {
        // buffer store for the stream
        byte[] buffer = new byte[1024];
        int len; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                len = mmInStream.read(buffer);
                if (len > 0) {
                    if (listener != null) {
                        listener.onNewData(buffer);
                    }
                }
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                if (listener != null) {
                    listener.onRunError(e);
                }
                break;
            }
        }
    }

    // Call this from the main activity to send data to the remote device.
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
            if (listener != null) {
                listener.onRunError(e);
            }
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
