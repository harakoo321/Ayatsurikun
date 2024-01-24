package com.mmp.ayatsurikun.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class DeviceControlViewModel extends ViewModel {
    private final ConnectionUseCase connectionUseCase;
    private final SignalUseCase signalUseCase;
    private final LiveData<byte[]> signal;
    private final MutableLiveData<Signal> longClickedSignal = new MutableLiveData<>();

    @Inject
    public DeviceControlViewModel(
            @ApplicationContext Context context,
            ConnectionUseCase connectionUseCase,
            SignalUseCase signalUseCase) {
        this.signal = ((App)context).getDevice().getSignal();
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
}
