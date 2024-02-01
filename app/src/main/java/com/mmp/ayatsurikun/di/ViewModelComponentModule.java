package com.mmp.ayatsurikun.di;

import com.mmp.ayatsurikun.repository.BluetoothDeviceRepositoryImpl;
import com.mmp.ayatsurikun.repository.DeviceRepository;
import com.mmp.ayatsurikun.repository.ScheduleRepository;
import com.mmp.ayatsurikun.repository.ScheduleRepositoryImpl;
import com.mmp.ayatsurikun.repository.SignalRepository;
import com.mmp.ayatsurikun.repository.SignalRepositoryImpl;
import com.mmp.ayatsurikun.repository.UsbDeviceRepositoryImpl;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.ConnectionUseCaseImpl;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCase;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCaseImpl;
import com.mmp.ayatsurikun.usecase.ScheduleUseCase;
import com.mmp.ayatsurikun.usecase.ScheduleUseCaseImpl;
import com.mmp.ayatsurikun.usecase.SignalUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCaseImpl;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ViewModelComponentModule {
    @Binds
    public abstract ScanDevicesUseCase bindScanDevicesUseCase(ScanDevicesUseCaseImpl scanDevicesUseCaseImpl);

    @Provides
    @Named("Usb")
    public static DeviceRepository provideUsbDeviceRepository(UsbDeviceRepositoryImpl usbDeviceRepositoryImpl) {
        return usbDeviceRepositoryImpl;
    }

    @Provides
    @Named("Bluetooth")
    public static DeviceRepository provideBluetoothDeviceRepository(BluetoothDeviceRepositoryImpl bluetoothDeviceRepositoryImpl) {
        return bluetoothDeviceRepositoryImpl;
    }

    @Binds
    @Singleton
    public abstract ConnectionUseCase bindConnectionUseCase(ConnectionUseCaseImpl connectionUseCaseImpl);

    @Binds
    public abstract SignalUseCase bindSignalUseCase(SignalUseCaseImpl signalUseCaseImpl);

    @Binds
    public abstract SignalRepository bindSignalRepository(SignalRepositoryImpl signalRepositoryImpl);

    @Binds
    public abstract ScheduleUseCase bindScheduleUseCase(ScheduleUseCaseImpl scheduleUseCaseImpl);

    @Binds
    public abstract ScheduleRepository bindScheduleRepository(ScheduleRepositoryImpl scheduleRepositoryImpl);
}
