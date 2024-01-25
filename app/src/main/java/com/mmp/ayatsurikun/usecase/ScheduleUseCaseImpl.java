package com.mmp.ayatsurikun.usecase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.repository.ScheduleRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ScheduleUseCaseImpl implements ScheduleUseCase {
    private final ScheduleRepository scheduleRepository;
    private final Context context;

    @Inject
    public ScheduleUseCaseImpl(
            ScheduleRepository scheduleRepository,
            @ApplicationContext Context context) {
        this.scheduleRepository = scheduleRepository;
        this.context = context;
    }

    @Override
    public LiveData<List<Schedule>> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule findById(int id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public void addSchedule(String deviceId, int signalId, LocalDateTime time) {
        try {
            int id = scheduleRepository.insert(new Schedule(0, deviceId, signalId, time));
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = createPendingIntent(id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC, time.toEpochSecond(ZoneOffset.ofHours(+9)), pendingIntent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, time.toEpochSecond(ZoneOffset.ofHours(+9)), pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSchedule(int id, String deviceId, int signalId, LocalDateTime time) {
        scheduleRepository.update(new Schedule(id, deviceId, signalId, time));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createPendingIntent(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC, time.toEpochSecond(ZoneOffset.ofHours(+9)), pendingIntent);
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, time.toEpochSecond(ZoneOffset.ofHours(+9)), pendingIntent);
        }
    }

    @Override
    public void cancelSchedule(Schedule schedule) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, schedule.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }

    private PendingIntent createPendingIntent(int id) {
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        intent.putExtra("id", id);
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}