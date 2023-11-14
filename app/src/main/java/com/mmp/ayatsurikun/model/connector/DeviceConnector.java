package com.mmp.ayatsurikun.model.connector;

public interface DeviceConnector {
    void setUp();
    void connect();
    void send(String str);
}
