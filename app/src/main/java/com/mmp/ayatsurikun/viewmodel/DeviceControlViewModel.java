package com.mmp.ayatsurikun.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class DeviceControlViewModel extends ViewModel {
    private final ConnectionUseCase connectionUseCase;
    private final SignalUseCase signalUseCase;
    private final LiveData<byte[]> signal;

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

    public void connect() {
        connectionUseCase.connect();
    }

    public void disconnect() {
        connectionUseCase.disconnect();
    }

    public void onBottomNavigationClick(int itemId) {
        if (itemId == R.id.bottom_menu_signal) {
            connectionUseCase.disconnect();
        } else if (itemId == R.id.bottom_menu_schedule) {
            connectionUseCase.connect();
        }
    }
}
