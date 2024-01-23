package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.repository.SignalRepository;
import com.mmp.ayatsurikun.repository.SignalRepositoryImpl;

import java.util.List;
import java.util.UUID;

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
        signalRepository.create(new Signal(UUID.randomUUID().toString(), name, signal));
    }

    @Override
    public void deleteSignal(Signal signal) {
        signalRepository.delete(signal);
    }
}
