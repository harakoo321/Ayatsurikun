package com.mmp.ayatsurikun.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SignalWithSchedules {
    @Embedded public Signal signal;
    @Relation(
            parentColumn = "id",
            entityColumn = "signal_id"
    )
    public List<Schedule> schedules;
}
