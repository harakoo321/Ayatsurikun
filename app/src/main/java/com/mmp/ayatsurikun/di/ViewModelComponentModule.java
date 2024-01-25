package com.mmp.ayatsurikun.di;

import com.mmp.ayatsurikun.repository.BluetoothDeviceRepositoryImpl;
import com.mmp.ayatsurikun.repository.DeviceRepository;
import com.mmp.ayatsurikun.repository.SignalRepository;
import com.mmp.ayatsurikun.repository.SignalRepositoryImpl;
import com.mmp.ayatsurikun.repository.UsbDeviceRepositoryImpl;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.ConnectionUseCaseImpl;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCase;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCaseImpl;
import com.mmp.ayatsurikun.usecase.ScheduleUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCaseImpl;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class ViewModelComponentModule {
    @Binds
    @ViewModelScoped
    public abstract ScanDevicesUseCase bindScanDevicesUseCase(ScanDevicesUseCaseImpl scanDevicesUseCaseImpl);

    @Provides
    @ViewModelScoped
    @Named("Usb")
    public static DeviceRepository provideUsbDeviceRepository(UsbDeviceRepositoryImpl usbDeviceRepositoryImpl) {
        return usbDeviceRepositoryImpl;
    }

    @Provides
    @ViewModelScoped
    @Named("Bluetooth")
    public static DeviceRepository provideBluetoothDeviceRepository(BluetoothDeviceRepositoryImpl bluetoothDeviceRepositoryImpl) {
        return bluetoothDeviceRepositoryImpl;
    }

    @Binds
    @ViewModelScoped
    public abstract ConnectionUseCase bindConnectionUseCase(ConnectionUseCaseImpl connectionUseCaseImpl);

    @Binds
    @ViewModelScoped
    public abstract SignalUseCase bindSignalUseCase(SignalUseCaseImpl signalUseCaseImpl);

    @Binds
    @ViewModelScoped
    public abstract SignalRepository bindSignalRepository(SignalRepositoryImpl signalRepositoryImpl);

    @Binds
    @ViewModelScoped
    public abstract ScheduleUseCase bindScheduleUseCase(SignalUseCaseImpl signalUseCaseImpl);

    @Binds
    @ViewModelScoped
    public abstract SignalRepository bindScheduleRepository(SignalRepositoryImpl signalRepositoryImpl);
}
