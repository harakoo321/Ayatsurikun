package com.mmp.ayatsurikun.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.Signal;

@Database(entities = {Signal.class, Schedule.class}, version = 1, exportSchema = false)
public abstract class SqliteDatabase extends RoomDatabase {
    public abstract SignalDao signalDao();

    public abstract ScheduleDao scheduleDao();
}
