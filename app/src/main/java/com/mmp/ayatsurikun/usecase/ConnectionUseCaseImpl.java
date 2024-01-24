package com.mmp.ayatsurikun.usecase;

import android.content.Context;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.model.Device;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ConnectionUseCaseImpl implements ConnectionUseCase{
    private final Device device;

    @Inject
    public ConnectionUseCaseImpl(@ApplicationContext Context context) {
        this.device = ((App)context).getDevice();
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
