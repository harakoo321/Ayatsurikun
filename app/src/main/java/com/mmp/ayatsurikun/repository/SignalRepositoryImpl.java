package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;
import com.mmp.ayatsurikun.db.SignalDao;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.model.SignalWithSchedules;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class SignalRepositoryImpl implements SignalRepository {
    private final SignalDao signalDao;
    private final ExecutorService exec = Executors.newCachedThreadPool();

    @Inject
    public SignalRepositoryImpl(SignalDao signalDao) {
        this.signalDao = signalDao;
    }

    @Override
    public void create(Signal signal) {
        exec.execute(() -> signalDao.insert(signal));
    }

    @Override
    public void delete(Signal signal) {
        exec.execute(() -> signalDao.delete(signal));
    }

    @Override
    public LiveData<List<Signal>> findAll() {
        return signalDao.findAll();
    }

    @Override
    public Signal findById(int id) {
        return signalDao.findById(id);
    }

    @Override
    public SignalWithSchedules findSignalWithSchedulesById(int signalId) {
        return signalDao.findSignalWithSchedulesById(signalId);
    }
}
