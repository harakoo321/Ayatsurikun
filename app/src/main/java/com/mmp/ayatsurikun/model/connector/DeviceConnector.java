package com.mmp.ayatsurikun.model.connector;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;

public interface DeviceConnector {
    void setUp(SignalButtonsContract contract);
    void connect();
    void disconnect();
    void send(byte[] signal);
    LiveData<byte[]> getSignal();
}
