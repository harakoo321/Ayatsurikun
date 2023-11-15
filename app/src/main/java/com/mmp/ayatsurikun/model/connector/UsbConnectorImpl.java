package com.mmp.ayatsurikun.model.connector;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.hoho.android.usbserial.BuildConfig;
import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.model.CustomProber;

import java.io.IOException;

public class UsbConnectorImpl implements DeviceConnector, SerialInputOutputManager.Listener {
    private enum UsbPermission { Unknown, Requested, Granted, Denied }
    private static final String INTENT_ACTION_GRANT_USB = "com.mmp.ayaturikun.GRANT_USB";
    private static final int WRITE_WAIT_MILLIS = 2000;
    private UsbPermission usbPermission = UsbPermission.Unknown;
    private UsbSerialPort usbSerialPort;
    private SerialInputOutputManager usbIoManager;
    private final BroadcastReceiver broadcastReceiver;
    private final Handler mainLooper;
    private final SignalButtonsContract contract;
    private final int deviceId, portNum, baudRate;
    private boolean connected = false;
    public UsbConnectorImpl(SignalButtonsContract contract, int deviceId, int portNum, int baudRate) {
        this.contract = contract;
        this.deviceId = deviceId;
        this.portNum = portNum;
        this.baudRate = baudRate;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    usbPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            ? UsbPermission.Granted : UsbPermission.Denied;
                    connect();
                }
            }
        };
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

    /*
     * Serial + UI
     */
    @Override
    public void setUp() {
        ((Activity)contract).registerReceiver(broadcastReceiver, new IntentFilter(INTENT_ACTION_GRANT_USB), Context.RECEIVER_NOT_EXPORTED);

        if(usbPermission == UsbPermission.Unknown || usbPermission == UsbPermission.Granted)
            mainLooper.post(this::connect);
        Toast.makeText((Activity)contract, "Setup Completed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connect() {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) ((Activity)contract).getSystemService(Context.USB_SERVICE);
        for(UsbDevice v : usbManager.getDeviceList().values())
            if(v.getDeviceId() == deviceId)
                device = v;
        if(device == null) {
            status("connection failed: device not found");
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            status("connection failed: no driver for device");
            return;
        }
        if(driver.getPorts().size() < portNum) {
            status("connection failed: not enough ports at device");
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            int flags = PendingIntent.FLAG_MUTABLE;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast((Activity)contract, 0, new Intent(INTENT_ACTION_GRANT_USB), flags);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                status("connection failed: permission denied");
            else
                status("connection failed: open failed");
            return;
        }

        try {
            usbSerialPort.open(usbConnection);
            try{
                usbSerialPort.setParameters(baudRate, 8, 1, UsbSerialPort.PARITY_NONE);
            }catch (UnsupportedOperationException e){
                status("unsupport setparameters");
            }
            usbIoManager = new SerialInputOutputManager(usbSerialPort, this);
            usbIoManager.start();
            status("connected");
            connected = true;
        } catch (Exception e) {
            status("connection failed: " + e.getMessage());
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        if(connected) {
            connected = false;
            ((Activity)contract).unregisterReceiver(broadcastReceiver);
            if(usbIoManager != null) {
                usbIoManager.setListener(null);
                usbIoManager.stop();
            }
            usbIoManager = null;
            try {
                usbSerialPort.close();
            } catch (IOException ignored) {}
            usbSerialPort = null;
        }
    }

    @Override
    public void send(String str) {
        if(!connected) {
            Toast.makeText((Activity)contract, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] data = (str + '\n').getBytes();
            usbSerialPort.write(data, WRITE_WAIT_MILLIS);

        } catch (Exception e) {
            onRunError(e);
        }
    }

    private void receive(byte[] data) {
        contract.addText("receive:" + new String(data) + "\n");
    }

    void status(String str) {
        contract.addText(str + "\n");
    }
}
