package com.mmp.ayatsurikun.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "signals")
public class Signal {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    @NonNull
    @ColumnInfo(name = "signal")
    private final byte[] signal;

    public Signal(@NonNull String name, @NonNull byte[] signal) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.signal = signal;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public byte[] getSignal() {
        return signal;
    }
}
