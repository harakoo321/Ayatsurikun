package com.mmp.ayatsurikun.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.model.connector.DeviceConnector;
import com.mmp.ayatsurikun.model.connector.UsbConnectorImpl;

import java.util.HashMap;
import java.util.Map;

public class SignalButtonsViewModel extends ViewModel {
    private MutableLiveData<byte[]> signal = new MutableLiveData<>();
    private Map<String, byte[]> signalHashMap = new HashMap<>();
    private final DeviceConnector deviceConnector;
    private final SignalButtonsContract contract;
    public SignalButtonsViewModel(SignalButtonsContract contract, int deviceId, int portNum, int baudRate) {
        this.contract = contract;
        deviceConnector = new UsbConnectorImpl(contract, deviceId, portNum, baudRate);
    }

    public void onSendButtonClick(View view) {
        String str = contract.getEditedText() + "\0";
        deviceConnector.send(str.getBytes());
        contract.addText("send:" + str + "\n");
    }

    public void setButtonText(String text) {
        contract.addButton(text);
        //signalHashMap.put(text, signal.getValue());
    }

    public void onSignalButtonClick(String text) {
        //deviceConnector.send(signalHashMap.get(text));
    }

    public void setUp() {
        deviceConnector.setUp();
    }

    public void disconnect() {
        deviceConnector.disconnect();
    }
}
