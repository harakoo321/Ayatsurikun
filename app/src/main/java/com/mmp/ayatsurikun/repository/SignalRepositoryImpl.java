package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;
import com.mmp.ayatsurikun.db.SignalDao;
import com.mmp.ayatsurikun.db.SqliteDatabase;
import com.mmp.ayatsurikun.model.Signal;

import java.util.List;

public class SignalRepositoryImpl implements SignalRepository {
    SqliteDatabase db;
    SignalDao signalDao;
    public SignalRepositoryImpl() {
        db = SqliteDatabase.getInstance();
        signalDao = db.signalDao();
    }

    public void create(Signal signal) {
        signalDao.insert(signal);
    }

    public void delete(Signal signal) {
        signalDao.delete(signal);
    }

    public LiveData<List<Signal>> findAll() {
        return signalDao.findAll();
    }
}
