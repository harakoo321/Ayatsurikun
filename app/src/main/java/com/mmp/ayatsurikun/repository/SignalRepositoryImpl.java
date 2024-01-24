package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;
import com.mmp.ayatsurikun.db.SignalDao;
import com.mmp.ayatsurikun.model.Signal;

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

    public void create(Signal signal) {
        exec.execute(() -> signalDao.insert(signal));
    }

    public void delete(Signal signal) {
        exec.execute(() -> signalDao.delete(signal));
    }

    public LiveData<List<Signal>> findAll() {
        return signalDao.findAll();
    }
}
