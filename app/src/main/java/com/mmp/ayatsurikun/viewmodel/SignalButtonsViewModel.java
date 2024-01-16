package com.mmp.ayatsurikun.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
    private SignalButtonsContract contract;
    private byte[] signal;
    public SignalButtonsViewModel(String deviceId, int portNum, int baudRate, ConnectionMethod connectionMethod) {
        if (connectionMethod == ConnectionMethod.USB_SERIAL) {
            deviceConnector = new UsbConnectorImpl(deviceId, portNum, baudRate);
        } else if (connectionMethod == ConnectionMethod.BLUETOOTH_SPP) {
            deviceConnector = new BluetoothConnectorImpl(deviceId);
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

    public void setUp(SignalButtonsContract contract) {
        this.contract = contract;
        deviceConnector.setUp(contract);
        Log.i("ButtonList", "resuming...");
        if (!signalHashMap.isEmpty()) {
            Log.i("ButtonList", "Not empty!");
            for (String text : signalHashMap.keySet()) {
                Log.i("ButtonList", "add:" + text);
                contract.addButton(text);
            }
        }
    }

    public void disconnect() {
        deviceConnector.disconnect();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final String deviceId;
        private final int portNum, baudRate;
        private final ConnectionMethod connectionMethod;
        public Factory(String deviceId, int portNum, int baudRate, ConnectionMethod connectionMethod) {
            this.deviceId = deviceId;
            this.portNum = portNum;
            this.baudRate = baudRate;
            this.connectionMethod = connectionMethod;
        }

        @SuppressWarnings("unchecked cast")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignalButtonsViewModel(deviceId, portNum, baudRate, connectionMethod);
        }
    }
}
