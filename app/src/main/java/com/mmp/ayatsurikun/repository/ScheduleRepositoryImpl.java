package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.db.ScheduleDao;
import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleDao scheduleDao;
    private final ExecutorService exec = Executors.newCachedThreadPool();
    @Inject
    public ScheduleRepositoryImpl(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @Override
    public LiveData<List<Schedule>> findAll() {
        return scheduleDao.findAll();
    }

    @Override
    public Schedule findById(int id) {
        return scheduleDao.findById(id);
    }

    @Override
    public int insert(Schedule schedule) throws Exception {
        return exec.submit(() -> scheduleDao.insert(schedule)).get().intValue();
    }

    @Override
    public void update(Schedule schedule) {
        exec.execute(() -> scheduleDao.update(schedule));
    }

    @Override
    public void delete(Schedule schedule) {
        exec.execute(() -> scheduleDao.delete(schedule));
    }

    @Override
    public ScheduleAndSignal findScheduleAndSignalById(int scheduleId) {
        try {
            return exec.submit(() -> scheduleDao.findScheduleAndSignalById(scheduleId)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ScheduleAndSignal> findAllScheduleAndSignal() {
        try {
            return exec.submit(scheduleDao::findAllScheduleAndSignal).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
