package com.mmp.ayatsurikun.usecase;

import com.mmp.ayatsurikun.model.Device;

public interface ConnectionUseCase {
    void connect(Device device);
    void waitUntilConnected();
    void disconnect();
    void send(byte[] signal);
    void clear();
}
