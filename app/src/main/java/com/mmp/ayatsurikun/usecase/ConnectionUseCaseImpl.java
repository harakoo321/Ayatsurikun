package com.mmp.ayatsurikun.usecase;

import com.mmp.ayatsurikun.model.Device;

public class ConnectionUseCaseImpl implements ConnectionUseCase{
    private final Device device;
    public ConnectionUseCaseImpl(Device device) {
        this.device = device;
    }
    @Override
    public void connect() {
        device.connect();
    }

    @Override
    public void disconnect() {
        device.disconnect();
    }

    @Override
    public void send(byte[] signal) {
        device.send(signal);
    }

    @Override
    public void clear() {
        device.clearSignal();
    }
}
