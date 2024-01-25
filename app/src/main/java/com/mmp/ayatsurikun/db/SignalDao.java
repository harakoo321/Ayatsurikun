package com.mmp.ayatsurikun.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.model.SignalWithSchedules;

import java.util.List;

@Dao
public interface SignalDao {
    @Query("SELECT * FROM signals")
    LiveData<List<Signal>> findAll();

    @Query("SELECT * FROM signals WHERE id = :id")
    Signal findById(int id);

    @Insert
    void insert(Signal signal);

    @Delete
    void delete(Signal signal);

    @Transaction
    @Query("SELECT * FROM signals WHERE id = :signalId")
    SignalWithSchedules findSignalWithSchedulesById(int signalId);
}
