package com.mmp.ayatsurikun.usecase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.model.Signal;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlarmManagerReceiver extends BroadcastReceiver {
    private final ScanDevicesUseCase scanDevicesUseCase;
    private final ConnectionUseCase connectionUseCase;
    private final SignalUseCase signalUseCase;
    private final ScheduleUseCase scheduleUseCase;
    @Inject
    public AlarmManagerReceiver(
            ScanDevicesUseCase scanDevicesUseCase,
            ConnectionUseCase connectionUseCase,
            SignalUseCase signalUseCase,
            ScheduleUseCase scheduleUseCase) {
        this.scanDevicesUseCase = scanDevicesUseCase;
        this.connectionUseCase = connectionUseCase;
        this.signalUseCase = signalUseCase;
        this.scheduleUseCase = scheduleUseCase;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        Schedule schedule = scheduleUseCase.findById(id);
        if (schedule == null) {
            Toast.makeText(context, R.string.schedule_not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        Device device = scanDevicesUseCase.scanDevice(schedule.getDeviceId());
        if (device == null) {
            Toast.makeText(context, R.string.not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        ((App)context.getApplicationContext()).setDevice(device);
        connectionUseCase.connect();
        Signal signal = signalUseCase.getSignalById(schedule.getId());
        if(signal != null) {
            connectionUseCase.send(signal.getSignal());
        }
        connectionUseCase.disconnect();
        scheduleUseCase.deleteSchedule(schedule);
    }
}
