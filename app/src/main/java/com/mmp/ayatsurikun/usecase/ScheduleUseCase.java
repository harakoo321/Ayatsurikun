package com.mmp.ayatsurikun.usecase;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleUseCase {
    LiveData<List<Schedule>> findAll();
    Schedule findById(int id);
    void addSchedule(String deviceId, int signalId, LocalDateTime time);
    void updateSchedule(int id, String deviceId, int signalId, LocalDateTime time);
    void cancelSchedule(Schedule schedule);
    void deleteSchedule(Schedule schedule);
}
