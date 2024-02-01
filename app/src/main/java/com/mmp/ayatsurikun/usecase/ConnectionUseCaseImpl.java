package com.mmp.ayatsurikun.usecase;

import com.mmp.ayatsurikun.model.Device;

import javax.inject.Inject;

public class ConnectionUseCaseImpl implements ConnectionUseCase{
    private Device device;

    @Inject
    public ConnectionUseCaseImpl() {
    }

    @Override
    public void connect(Device device) {
        this.device = device;
        device.connect();
    }

    @Override
    public void waitUntilConnected() {
        device.waitUntilConnected();
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
