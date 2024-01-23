package com.mmp.ayatsurikun.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.contract.SignalButtonsContract;
import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.ConnectionUseCaseImpl;

import java.util.HashMap;
import java.util.Map;

public class SignalButtonsViewModel extends ViewModel {
    private final Device device;
    private final ConnectionUseCase connectionUseCase;
    private final Map<String, byte[]> signalHashMap = new HashMap<>();
    private byte[] signal;
    public SignalButtonsViewModel(Device device) {
        this.device = device;
        connectionUseCase = new ConnectionUseCaseImpl(device);
    }

    public LiveData<byte[]> getSignal() {
        return device.getSignal();
    }

    public void setSignal(byte[] signal) {
        this.signal = signal;
    }

    public void setButtonText(String text, SignalButtonsContract contract) {
        contract.addButton(text);
        signalHashMap.put(text, signal);
    }

    public void onSignalButtonClick(String text) {
        device.send(signalHashMap.get(text));
    }

    public void connect() {
        connectionUseCase.connect();
    }

    public void disconnect() {
        connectionUseCase.disconnect();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Device device;
        public Factory(Device device) {
            this.device = device;
        }

        @SuppressWarnings("unchecked cast")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SignalButtonsViewModel(device);
        }
    }
}
