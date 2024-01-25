package com.mmp.ayatsurikun.db;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class EntityTypeConverter {
    @TypeConverter
    public String fromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    @TypeConverter
    public LocalDateTime toLocalDateTime(String localDateTime) {
        return LocalDateTime.parse(localDateTime);
    }
}
