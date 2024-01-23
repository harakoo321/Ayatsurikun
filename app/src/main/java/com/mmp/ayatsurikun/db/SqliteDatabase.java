package com.mmp.ayatsurikun.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mmp.ayatsurikun.model.Signal;

@Database(entities = {Signal.class}, version = 1, exportSchema = false)
public abstract class SqliteDatabase extends RoomDatabase {
    private static volatile SqliteDatabase INSTANCE;
    public abstract SignalDao signalDao();

    public static SqliteDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (SqliteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            com.mmp.ayatsurikun.App.ContextProvider.getContext(),
                            SqliteDatabase.class, "sqlite_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
