package com.mmp.ayatsurikun.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.repository.BluetoothDeviceRepositoryImpl;
import com.mmp.ayatsurikun.repository.DeviceRepository;
import com.mmp.ayatsurikun.repository.UsbDeviceRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class DeviceListViewModel extends ViewModel {
    private final List<DeviceRepository> deviceRepositories = new ArrayList<>();
    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>();
    private MutableLiveData<Device> selectedDevice;


    //@Inject
    public DeviceListViewModel() {
        deviceRepositories.add(new UsbDeviceRepositoryImpl());
        deviceRepositories.add(new BluetoothDeviceRepositoryImpl());
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
        List<Device> deviceItems = new ArrayList<>();
        for (DeviceRepository deviceRepository : deviceRepositories) {
            List<Device> items = deviceRepository.scanDevices();
            if (items != null) deviceItems.addAll(items);
        }
        devices.setValue(deviceItems);
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
