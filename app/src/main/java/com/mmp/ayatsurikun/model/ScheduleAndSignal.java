package com.mmp.ayatsurikun.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ScheduleAndSignal {
    @Embedded public Schedule schedule;
    @Relation(
            parentColumn = "signal_id",
            entityColumn = "id"
    )
    public Signal signal;
}
