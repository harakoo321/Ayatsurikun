package com.mmp.ayatsurikun.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "schedules",
        foreignKeys = @ForeignKey(
                entity = Signal.class,
                parentColumns = "id",
                childColumns = "signal_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private final int id;

    @NonNull
    @ColumnInfo(name = "device_id")
    private final String deviceId;

    @ColumnInfo(name = "signal_id")
    private final int signalId;

    @NonNull
    @ColumnInfo(name = "time")
    private final LocalDateTime time;

    public Schedule(int id, @NonNull String deviceId, int signalId, @NonNull LocalDateTime time) {
        this.id = id;
        this.deviceId = deviceId;
        this.signalId = signalId;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDeviceId() {
        return deviceId;
    }

    public int getSignalId() {
        return signalId;
    }

    @NonNull
    public LocalDateTime getTime() {
        return time;
    }
}
