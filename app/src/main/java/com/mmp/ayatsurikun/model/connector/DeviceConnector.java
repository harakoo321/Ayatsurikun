package com.mmp.ayatsurikun.model.connector;

public interface DeviceConnector {
    void setUp();
    void connect();
    void disconnect();
    void send(String str);
}
