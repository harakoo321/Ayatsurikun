package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DeviceListViewModel extends ViewModel {
    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>();
    private MutableLiveData<Device> selectedDevice;
    private final ScanDevicesUseCase scanDevicesUseCase;

    @Inject
    public DeviceListViewModel(ScanDevicesUseCase scanDevicesUseCase) {
        this.scanDevicesUseCase = scanDevicesUseCase;
    }

    public LiveData<List<Device>> getDevices() {
        return devices;
    }

    public LiveData<Device> getSelectedDevice() {
        return selectedDevice;
    }

    public void clearSelectedDevice() {
        selectedDevice = new MutableLiveData<>();
    }

    public void loadDevices() {
        devices.setValue(scanDevicesUseCase.scanAllDevices());
    }

    public void onItemClick(Device device) {
        selectedDevice.setValue(device);
    }
}
