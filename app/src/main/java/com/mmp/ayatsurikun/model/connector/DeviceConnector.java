package com.mmp.ayatsurikun.model.connector;

import androidx.lifecycle.LiveData;

public interface DeviceConnector {
    void setUp();
    void connect();
    void disconnect();
    void send(byte[] signal);
    LiveData<byte[]> getSignal();
}
