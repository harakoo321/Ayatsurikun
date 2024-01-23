package com.mmp.ayatsurikun.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mmp.ayatsurikun.model.Signal;

import java.util.List;

@Dao
public interface SignalDao {
    @Query("SELECT * FROM signals")
    LiveData<List<Signal>> findAll();

    @Insert
    void insert(Signal signal);

    @Delete
    void delete(Signal signal);
}
