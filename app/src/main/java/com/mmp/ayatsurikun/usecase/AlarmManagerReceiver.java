package com.mmp.ayatsurikun.usecase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlarmManagerReceiver extends BroadcastReceiver {
    @Inject
    ScanDevicesUseCase scanDevicesUseCase;
    @Inject
    ConnectionUseCase connectionUseCase;
    @Inject
    SignalUseCase signalUseCase;
    @Inject
    ScheduleUseCase scheduleUseCase;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        ScheduleAndSignal scheduleAndSignal = scheduleUseCase.findScheduleAndSignalById(id);
        if (scheduleAndSignal == null) {
            Toast.makeText(context, R.string.schedule_not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        Device device = scanDevicesUseCase.scanDevice(scheduleAndSignal.schedule.getDeviceId());
        if (device == null) {
            Toast.makeText(context, R.string.not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!device.isConnected()) {
            connectionUseCase.connect(device);
            connectionUseCase.waitUntilConnected();
        }
        if(scheduleAndSignal.signal != null) {
            connectionUseCase.send(scheduleAndSignal.signal.getSignal());
        }
        connectionUseCase.disconnect();
        scheduleUseCase.deleteSchedule(scheduleAndSignal.schedule);
    }
}
