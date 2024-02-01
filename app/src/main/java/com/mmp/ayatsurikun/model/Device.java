package com.mmp.ayatsurikun.model;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.util.ConnectionType;

public interface Device {
    String getId();
    String getName();
    ConnectionType getConnectionType();
    LiveData<byte[]> getSignal();
    void clearSignal();
    void connect();
    boolean isConnected();
    void waitUntilConnected();
    void disconnect();
    void send(byte[] signal);
    boolean equals(Object o);
}
