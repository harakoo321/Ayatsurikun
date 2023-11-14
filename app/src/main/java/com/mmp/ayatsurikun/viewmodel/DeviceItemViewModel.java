package com.mmp.ayatsurikun.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.model.scanner.DeviceScanner;

public class DeviceItemViewModel extends ViewModel {
    public MutableLiveData<String> driverName = new MutableLiveData<>();
    public MutableLiveData<String> productName = new MutableLiveData<>();
    public MutableLiveData<String> deviceName = new MutableLiveData<>();
    private final DeviceListViewContract view;
    private int deviceId, portNum;

    public DeviceItemViewModel(DeviceListViewContract view) {
        this.view = view;
    }

    public void loadItem(DeviceScanner.DeviceItem item) {
        deviceName.setValue(item.deviceName);
        driverName.setValue("driver:" + item.driverName);
        productName.setValue("product:" + item.productName);
        deviceId = item.deviceId;
        portNum = item.port;
    }

    public void onItemClick(View itemView) {
        view.startSignalButtonsActivity(deviceId, portNum);
    }
}
