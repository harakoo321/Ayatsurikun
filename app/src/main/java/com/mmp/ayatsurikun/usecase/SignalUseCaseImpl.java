package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.repository.SignalRepository;

import java.util.List;

import javax.inject.Inject;

public class SignalUseCaseImpl implements SignalUseCase {
    private final SignalRepository signalRepository;

    @Inject
    public SignalUseCaseImpl(SignalRepository signalRepository) {
        this.signalRepository = signalRepository;
    }

    @Override
    public LiveData<List<Signal>> getAllSignals() {
        return signalRepository.findAll();
    }

    @Override
    public void addSignal(String name, byte[] signal) {
        signalRepository.create(new Signal(0, name, signal));
    }

    @Override
    public void deleteSignal(Signal signal) {
        signalRepository.delete(signal);
    }
}
