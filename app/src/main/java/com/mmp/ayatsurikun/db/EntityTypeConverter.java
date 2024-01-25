package com.mmp.ayatsurikun.db;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.util.UUID;

public class EntityTypeConverter {
    @TypeConverter
    public String fromUUID(UUID uuid) {
        return uuid.toString();
    }

    @TypeConverter
    public UUID toUUID(String uuid) {
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public String fromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    @TypeConverter
    public LocalDateTime toLocalDateTime(String localDateTime) {
        return LocalDateTime.parse(localDateTime);
    }
}
