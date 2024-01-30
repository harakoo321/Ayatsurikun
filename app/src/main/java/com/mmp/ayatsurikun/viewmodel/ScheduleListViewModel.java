package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.usecase.ScheduleUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ScheduleListViewModel extends ViewModel {
    private final ScheduleUseCase scheduleUseCase;
    private final SignalUseCase signalUseCase;
    private final MutableLiveData<Schedule> longClickedSchedule = new MutableLiveData<>();

    @Inject
    public ScheduleListViewModel(ScheduleUseCase scheduleUseCase, SignalUseCase signalUseCase) {
        this.scheduleUseCase = scheduleUseCase;
        this.signalUseCase = signalUseCase;
    }

    public LiveData<List<Schedule>> getAllSchedules() {
        return scheduleUseCase.findAll();
    }

    public LiveData<List<ScheduleAndSignal>> getAllSchedulesAndSignals() {
        return scheduleUseCase.findAllScheduleAndSignal();
    }

    public void addSchedule(String deviceId, Signal signal, long dateTime) {
        scheduleUseCase.addSchedule(deviceId, signal, dateTime);
    }

    public void updateSchedule(int id, String deviceId, Signal signal, long dateTime) {
        scheduleUseCase.updateSchedule(id, deviceId, signal, dateTime);
    }

    public void deleteSchedule(Schedule schedule) {
        scheduleUseCase.cancelSchedule(schedule);
        scheduleUseCase.deleteSchedule(schedule);
    }

    public LiveData<List<Signal>> getAllSignals() {
        return signalUseCase.getAllSignals();
    }

    public Signal getSignalById(int id) {
        return signalUseCase.getSignalById(id);
    }

    public LiveData<Schedule> getLongClickedSchedule() {
        return longClickedSchedule;
    }

    public boolean onItemLongClick(Schedule schedule) {
        longClickedSchedule.setValue(schedule);
        return true;
    }

    public void clearLongClickedSchedule() {
        longClickedSchedule.setValue(null);
    }
}