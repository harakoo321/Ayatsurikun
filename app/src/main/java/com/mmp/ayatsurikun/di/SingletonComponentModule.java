package com.mmp.ayatsurikun.di;

import android.content.Context;

import androidx.room.Room;

import com.mmp.ayatsurikun.db.ScheduleDao;
import com.mmp.ayatsurikun.db.SignalDao;
import com.mmp.ayatsurikun.db.SqliteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SingletonComponentModule {
    @Provides
    @Singleton
    public static SqliteDatabase provideDb(@ApplicationContext Context context) {
        synchronized (SqliteDatabase.class) {
            return Room.databaseBuilder(
                    context,
                    SqliteDatabase.class, "sqlite_db").build();
        }
    }

    @Provides
    @Singleton
    public static SignalDao provideSignalDao(SqliteDatabase db) {
        return db.signalDao();
    }

    @Provides
    @Singleton
    public static ScheduleDao provideScheduleDao(SqliteDatabase db) {
        return db.scheduleDao();
    }
}
