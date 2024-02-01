package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DeviceControlViewModel extends ViewModel {
    private final ConnectionUseCase connectionUseCase;
    private final SignalUseCase signalUseCase;
    private LiveData<byte[]> signal;

    @Inject
    public DeviceControlViewModel(
            ConnectionUseCase connectionUseCase,
            SignalUseCase signalUseCase) {
        this.connectionUseCase = connectionUseCase;
        this.signalUseCase = signalUseCase;
    }

    public LiveData<byte[]> getSignal() {
        return signal;
    }

    public void addSignal(String text, byte[] signal) {
        signalUseCase.addSignal(text, signal);
        connectionUseCase.clear();
    }

    public void cancel() {
        connectionUseCase.clear();
    }

    public void connect(Device device) {
        signal = device.getSignal();
        connectionUseCase.connect(device);
    }

    public void disconnect() {
        connectionUseCase.disconnect();
    }
}
