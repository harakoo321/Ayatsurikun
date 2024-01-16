package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.model.ConnectionMethod;
import com.mmp.ayatsurikun.model.Device;

public class DeviceItemViewModel extends ViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> id = new MutableLiveData<>();
    public MutableLiveData<String> connection = new MutableLiveData<>();
    private final DeviceListViewContract view;
    private ConnectionMethod connectionMethod;
    private int portNum;

    public DeviceItemViewModel(DeviceListViewContract view) {
        this.view = view;
    }

    public void loadItem(Device device) {
        name.setValue(device.getName());
        id.setValue("id: " + device.getId());
        connection.setValue("connection: " + device.getConnectionMethod());
        portNum = device.getPort();
        connectionMethod = device.getConnectionMethod();
    }

    public void onItemClick() {
        view.startSignalButtonsActivity(name.getValue(), portNum, connectionMethod);
    }
}
