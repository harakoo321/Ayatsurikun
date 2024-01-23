package com.mmp.ayatsurikun.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.ConnectionUseCaseImpl;
import com.mmp.ayatsurikun.usecase.SignalUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCaseImpl;

import java.util.List;

public class DeviceControlViewModel extends ViewModel {
    private final ConnectionUseCase connectionUseCase;
    private final SignalUseCase signalUseCase;
    private final LiveData<byte[]> signal;
    private final MutableLiveData<Signal> longClickedSignal = new MutableLiveData<>();

    public DeviceControlViewModel(Device device) {
        this.signal = device.getSignal();
        connectionUseCase = new ConnectionUseCaseImpl(device);
        signalUseCase = new SignalUseCaseImpl();
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

    public LiveData<Signal> getLongClickedSignal() {
        return longClickedSignal;
    }

    public void clearLongClickedSignal() {
        longClickedSignal.setValue(null);
    }

    public LiveData<List<Signal>> getAllSignals() {
        return signalUseCase.getAllSignals();
    }

    public void connect() {
        connectionUseCase.connect();
    }

    public void disconnect() {
        connectionUseCase.disconnect();
    }

    public void onItemClick(Signal signal) {
        connectionUseCase.send(signal.getSignal());
    }

    public boolean onItemLongClick(Signal signal) {
        longClickedSignal.setValue(signal);
        return true;
    }

    public void deleteSignal(Signal signal) {
        signalUseCase.deleteSignal(signal);
        longClickedSignal.setValue(null);
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
            return (T) new DeviceControlViewModel(device);
        }
    }
}
