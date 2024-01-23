package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.repository.SignalRepository;
import com.mmp.ayatsurikun.repository.SignalRepositoryImpl;

import java.util.List;

public class SignalUseCaseImpl implements SignalUseCase {
    private final SignalRepository signalRepository;

    public SignalUseCaseImpl() {
        signalRepository = new SignalRepositoryImpl();
    }

    @Override
    public LiveData<List<Signal>> getAllSignals() {
        return signalRepository.findAll();
    }

    @Override
    public void addSignal(String name, byte[] signal) {
        signalRepository.create(new Signal(name, signal));
    }

    @Override
    public void deleteSignal(Signal signal) {
        signalRepository.delete(signal);
    }
}
