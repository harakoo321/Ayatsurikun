package com.mmp.ayatsurikun.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.model.DeviceScanner;

public class DeviceItemViewModel extends ViewModel {
    public MutableLiveData<String> vendorId = new MutableLiveData<>();
    public MutableLiveData<String> productName = new MutableLiveData<>();
    public MutableLiveData<String> deviceName = new MutableLiveData<>();
    DeviceListViewContract view;

    public DeviceItemViewModel(DeviceListViewContract view) {
        this.view = view;
    }

    public void loadItem(DeviceScanner.DeviceItem item) {
        deviceName.setValue(item.deviceName);
        vendorId.setValue("vendor:" + item.vendorId);
        productName.setValue("product:" + item.productName);
    }

    public void onItemClick(View itemView) {

    }
}
