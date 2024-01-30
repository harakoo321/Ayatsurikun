package com.mmp.ayatsurikun.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedules")
    LiveData<List<Schedule>> findAll();

    @Query("SELECT * FROM schedules WHERE id = :id")
    Schedule findById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Schedule schedule);

    @Update
    void update(Schedule schedule);

    @Delete
    void delete(Schedule schedule);

    @Transaction
    @Query("SELECT * FROM schedules WHERE id = :scheduleId")
    ScheduleAndSignal findScheduleAndSignalById(int scheduleId);

    @Transaction
    @Query("SELECT * FROM schedules")
    LiveData<List<ScheduleAndSignal>> findAllScheduleAndSignal();

}
