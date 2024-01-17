package com.mmp.ayatsurikun.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.model.scanner.BluetoothDeviceScannerImpl;
import com.mmp.ayatsurikun.model.scanner.DeviceScanner;
import com.mmp.ayatsurikun.model.scanner.UsbDeviceScannerImpl;

import java.util.ArrayList;
import java.util.List;

public class DeviceListViewModel extends ViewModel {
    private final List<DeviceScanner> deviceScanners = new ArrayList<>();
    private final MutableLiveData<List<Device>> _devices = new MutableLiveData<>();
    public LiveData<List<Device>> devices = Transformations.distinctUntilChanged(_devices);
    private final MutableLiveData<Device> selectedDevice = new MutableLiveData<>();

    //@Inject
    public DeviceListViewModel() {
        deviceScanners.add(new UsbDeviceScannerImpl());
        deviceScanners.add(new BluetoothDeviceScannerImpl());
    }

    public LiveData<Device> getSelectedDevice() {
        return selectedDevice;
    }

    public void loadDevices() {
        List<Device> deviceItems = new ArrayList<>();
        for (DeviceScanner deviceScanner : deviceScanners) {
            List<Device> items = deviceScanner.scanDevices();
            if (items != null) deviceItems.addAll(items);
        }
        _devices.setValue(deviceItems);
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
