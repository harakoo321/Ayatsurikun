package com.mmp.ayatsurikun.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.model.connector.DeviceConnector;
import com.mmp.ayatsurikun.model.connector.UsbConnectorImpl;

import java.util.HashMap;
import java.util.Map;

public class SignalButtonsViewModel extends ViewModel {
    private Map<String, byte[]> signalHashMap = new HashMap<>();
    private final DeviceConnector deviceConnector;
    private final SignalButtonsContract contract;
    private byte[] signal;
    public SignalButtonsViewModel(SignalButtonsContract contract, int deviceId, int portNum, int baudRate) {
        this.contract = contract;
        deviceConnector = new UsbConnectorImpl(contract, deviceId, portNum, baudRate);
    }

    public LiveData<byte[]> getSignal() {
        return deviceConnector.getSignal();
    }

    public void setSignal(byte[] signal) {
        this.signal = signal;
    }

    public void onSendButtonClick(View view) {
        String str = contract.getEditedText() + "\n";
        deviceConnector.send(str.getBytes());
        contract.addText("send:" + str);
    }

    public void setButtonText(String text) {
        contract.addButton(text);
        signalHashMap.put(text, signal);
    }

    public void onSignalButtonClick(String text) {
        contract.addText("send:" + new String(signalHashMap.get(text)));
        deviceConnector.send(signalHashMap.get(text));
    }

    public void setUp() {
        deviceConnector.setUp();
    }

    public void disconnect() {
        deviceConnector.disconnect();
    }
}
