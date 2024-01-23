package com.mmp.ayatsurikun.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

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

    public Signal(@NonNull String id, @NonNull String name, @NonNull byte[] signal) {
        this.id = id;
        this.name = name;
        this.signal = signal;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public byte[] getSignal() {
        return signal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Signal)) return false;
        Signal signal = (Signal) o;
        return this.id.equals(signal.id) &&
                this.name.equals(signal.name) &&
                Arrays.equals(this.signal, signal.getSignal());
    }
}
