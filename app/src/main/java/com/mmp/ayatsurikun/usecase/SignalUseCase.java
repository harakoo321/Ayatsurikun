package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Signal;

import java.util.List;

public interface SignalUseCase {
    LiveData<List<Signal>> getAllSignals();
    List<Signal> getAllSignalsSync();
    void addSignal(String name, byte[] signal);
    void deleteSignal(Signal signal);
}
