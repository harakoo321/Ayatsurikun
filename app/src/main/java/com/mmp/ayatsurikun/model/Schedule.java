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

    @ColumnInfo(name = "signal_id")
    private final int signalId;

    @NonNull
    @ColumnInfo(name = "time")
    private final LocalDateTime time;

    public Schedule(int id, int signalId, @NonNull LocalDateTime time) {
        this.id = id;
        this.signalId = signalId;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getSignalId() {
        return signalId;
    }

    @NonNull
    public LocalDateTime getTime() {
        return time;
    }
}
