package com.mmp.ayatsurikun.usecase;

public interface ConnectionUseCase {
    void connect();
    void disconnect();
    void send(byte[] signal);
    void clear();
}
