package com.mmp.ayatsurikun.model;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.BuildConfig;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.util.ConnectionType;
import com.mmp.ayatsurikun.util.CustomProber;

import java.io.IOException;
import java.nio.ByteBuffer;

public class UsbDevice implements Device, SerialInputOutputManager.Listener {
    private static final String TAG = UsbDevice.class.getSimpleName();
    private final String id;
    private final String name;
    private final int port;
    private final ConnectionType connectionType;
    private enum UsbPermission { Unknown, Requested, Granted, Denied }
    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB";
    private static final int WRITE_WAIT_MILLIS = 2000;
    private UsbPermission usbPermission = UsbPermission.Unknown;
    private UsbSerialPort usbSerialPort;
    private SerialInputOutputManager usbIoManager;
    private final BroadcastReceiver broadcastReceiver;
    private final Handler mainLooper;
    private boolean connected = false;
    private final MutableLiveData<byte[]> signal = new MutableLiveData<>();
    private byte[] data;

    public UsbDevice(String id, String name, int port, ConnectionType connectionType) {
        this.id = id;
        this.name = name;
        this.port = port;
        this.connectionType = connectionType;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    usbPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            ? UsbPermission.Granted : UsbPermission.Denied;
                    connect();
                }
            }
        };
        mainLooper = new Handler(Looper.getMainLooper());
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
    public void onNewData(byte[] data) {
        mainLooper.post(() -> receive(data));
    }

    @Override
    public void onRunError(Exception e) {
        mainLooper.post(this::disconnect);
    }

    /*
     * Serial + UI
     */
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void connect() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            App.ContextProvider.getContext().registerReceiver(
                    broadcastReceiver,
                    new IntentFilter(INTENT_ACTION_GRANT_USB),
                    Context.RECEIVER_NOT_EXPORTED
            );
        } else {
            App.ContextProvider.getContext().registerReceiver(
                    broadcastReceiver,
                    new IntentFilter(INTENT_ACTION_GRANT_USB)
            );
        }
        android.hardware.usb.UsbDevice device = null;
        UsbManager usbManager =
                (UsbManager) App.ContextProvider.getContext().getSystemService(Context.USB_SERVICE);
        for(android.hardware.usb.UsbDevice v : usbManager.getDeviceList().values())
            if(v.getDeviceName().equals(id))
                device = v;
        if(device == null) {
            Toast.makeText(App.ContextProvider.getContext(), R.string.not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            showToast(R.string.driver_not_found);
            return;
        }
        if(driver.getPorts().size() < port) {
            showToast(R.string.port_not_found);
            return;
        }
        usbSerialPort = driver.getPorts().get(port);
        UsbDeviceConnection usbConnection = null;
        try{
            usbConnection = usbManager.openDevice(driver.getDevice());
        } catch (SecurityException e) {
            showToast(R.string.permission_denied);
        }
        if(usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            int flags = PendingIntent.FLAG_IMMUTABLE;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(
                    App.ContextProvider.getContext(),
                    0,
                    new Intent(INTENT_ACTION_GRANT_USB),
                    flags
            );
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice())) {
                showToast(R.string.permission_denied);
            }
            else {
                showToast(R.string.connection_failed);
            }
            return;
        }

        try {
            usbSerialPort.open(usbConnection);
            try{
                usbSerialPort.setParameters(115200, 8, 1, UsbSerialPort.PARITY_NONE);
            }catch (UnsupportedOperationException e){
                showToast(R.string.connection_failed);
                return;
            }
            usbIoManager = new SerialInputOutputManager(usbSerialPort, this);
            usbIoManager.start();
            showToast(R.string.connected);
            connected = true;
        } catch (Exception e) {
            onRunError(e);
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void waitUntilConnected() {}

    @Override
    public void disconnect() {
        if(connected) {
            connected = false;
            App.ContextProvider.getContext().unregisterReceiver(broadcastReceiver);
            if(usbIoManager != null) {
                usbIoManager.setListener(null);
                usbIoManager.stop();
            }
            usbIoManager = null;
            try {
                usbSerialPort.close();
            } catch (IOException ignored) {}
            usbSerialPort = null;
            showToast(R.string.disconnected);
            return;
        }
        showToast(R.string.not_connected);
    }

    @Override
    public void send(byte[] signal) {
        if(!connected) {
            showToast(R.string.not_connected);
            return;
        }
        try {
            usbSerialPort.write(signal, WRITE_WAIT_MILLIS);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsbDevice)) return false;
        UsbDevice that = (UsbDevice) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                port == that.port &&
                connectionType == that.connectionType;
    }

    private void showToast(int resId) {
        Toast.makeText(App.ContextProvider.getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
