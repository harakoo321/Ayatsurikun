package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;
import com.mmp.ayatsurikun.model.Signal;

import java.util.List;

public interface ScheduleUseCase {
    LiveData<List<ScheduleAndSignal>> findAllScheduleAndSignal();

    ScheduleAndSignal findScheduleAndSignalById(int scheduleId);

    void addSchedule(String deviceId, Signal signal, long dateTime);

    void updateSchedule(int id, String deviceId, Signal signal, long dateTime);

    void cancelSchedule(Schedule schedule);
    void deleteSchedule(Schedule schedule);
}
