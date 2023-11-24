package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.model.ConnectionMethod;
import com.mmp.ayatsurikun.model.connector.BluetoothConnectorImpl;
import com.mmp.ayatsurikun.model.connector.DeviceConnector;
import com.mmp.ayatsurikun.model.connector.UsbConnectorImpl;

import java.util.HashMap;
import java.util.Map;

public class SignalButtonsViewModel extends ViewModel {
    private final Map<String, byte[]> signalHashMap = new HashMap<>();
    private final DeviceConnector deviceConnector;
    private final SignalButtonsContract contract;
    private byte[] signal;
    public SignalButtonsViewModel(SignalButtonsContract contract, String deviceId, int portNum, int baudRate, String connectionMethod) {
        this.contract = contract;
        if (connectionMethod.equals(ConnectionMethod.USB_SERIAL)) {
            deviceConnector = new UsbConnectorImpl(contract, deviceId, portNum, baudRate);
        } else if (connectionMethod.equals(ConnectionMethod.BLUETOOTH_SPP)) {
            deviceConnector = new BluetoothConnectorImpl(contract, deviceId);
        } else {
            deviceConnector = null;
        }
    }

    public LiveData<byte[]> getSignal() {
        return deviceConnector.getSignal();
    }

    public void setSignal(byte[] signal) {
        this.signal = signal;
    }

    public void setButtonText(String text) {
        contract.addButton(text);
        signalHashMap.put(text, signal);
    }

    public void onSignalButtonClick(String text) {
        deviceConnector.send(signalHashMap.get(text));
    }

    public void setUp() {
        deviceConnector.setUp();
    }

    public void disconnect() {
        deviceConnector.disconnect();
    }
}
