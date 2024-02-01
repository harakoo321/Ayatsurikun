package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;

import java.util.List;

public interface ScheduleRepository {
    int insert(Schedule schedule) throws Exception;

    void update(Schedule schedule);

    void delete(Schedule schedule);

    ScheduleAndSignal findScheduleAndSignalById(int scheduleId);

    LiveData<List<ScheduleAndSignal>> findAllScheduleAndSignal();
}
