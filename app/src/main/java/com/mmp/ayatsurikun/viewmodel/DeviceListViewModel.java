package com.mmp.ayatsurikun.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCase;
import com.mmp.ayatsurikun.usecase.ScanDevicesUseCaseImpl;

import java.util.List;

public class DeviceListViewModel extends ViewModel {
    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>();
    private MutableLiveData<Device> selectedDevice;
    private final ScanDevicesUseCase scanDevicesUseCase;

    //@Inject
    public DeviceListViewModel() {
        scanDevicesUseCase = new ScanDevicesUseCaseImpl();
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

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @SuppressWarnings("unchecked cast")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DeviceListViewModel();
        }
    }
}
