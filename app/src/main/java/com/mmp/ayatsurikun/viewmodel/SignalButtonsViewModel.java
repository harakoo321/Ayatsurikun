package com.mmp.ayatsurikun.viewmodel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.model.connector.DeviceConnector;
import com.mmp.ayatsurikun.model.connector.UsbConnectorImpl;

public class SignalButtonsViewModel extends ViewModel {
    private final DeviceConnector deviceConnector;
    private final SignalButtonsContract contract;
    public SignalButtonsViewModel(SignalButtonsContract contract, int deviceId, int portNum, int baudRate) {
        this.contract = contract;
        deviceConnector = new UsbConnectorImpl(contract, deviceId, portNum, baudRate);
    }

    public void onClick(View view) {
        String str = contract.getEditedText();
        deviceConnector.send(str);
        contract.addText("send:" + str + "\n");
    }

    public void setUp() {
        deviceConnector.setUp();
    }
}
