package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Schedule;

import java.util.List;

public interface ScheduleRepository {
    LiveData<List<Schedule>> findAll();

    LiveData<Schedule> findById(int id);

    int insert(Schedule schedule) throws Exception;

    void update(Schedule schedule);

    void delete(Schedule schedule);
}
