package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.model.SignalWithSchedules;
import com.mmp.ayatsurikun.repository.SignalRepository;

import java.util.List;

import javax.inject.Inject;

public class SignalUseCaseImpl implements SignalUseCase {
    private final SignalRepository signalRepository;
    private final ScheduleUseCase scheduleUseCase;

    @Inject
    public SignalUseCaseImpl(SignalRepository signalRepository, ScheduleUseCase scheduleUseCase) {
        this.signalRepository = signalRepository;
        this.scheduleUseCase = scheduleUseCase;
    }

    @Override
    public LiveData<List<Signal>> getAllSignals() {
        return signalRepository.findAll();
    }

    @Override
    public List<Signal> getAllSignalsSync() {
        return signalRepository.findAllSync();
    }

    @Override
    public void addSignal(String name, byte[] signal) {
        signalRepository.create(new Signal(0, name, signal));
    }

    @Override
    public void deleteSignal(Signal signal) {
        SignalWithSchedules signalWithSchedules = signalRepository.findSignalWithSchedulesById(signal.getId());
        if (signalWithSchedules == null) return;
        signalWithSchedules.schedules.forEach(scheduleUseCase::cancelSchedule);
        signalRepository.delete(signal);
    }
}
