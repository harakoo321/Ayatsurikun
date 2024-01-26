package com.mmp.ayatsurikun.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "time")
    private final long dateTime;

    public Schedule(int id, @NonNull String deviceId, int signalId, long dateTime) {
        this.id = id;
        this.deviceId = deviceId;
        this.signalId = signalId;
        this.dateTime = dateTime;
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

    public long getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule other = (Schedule)o;
        return id == other.id &&
                deviceId.equals(other.deviceId) &&
                signalId == other.signalId &&
                dateTime == other.dateTime;
    }
}
