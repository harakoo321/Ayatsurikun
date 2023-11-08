package com.mmp.ayatsurikun.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.model.DeviceService;
import com.mmp.ayatsurikun.view.MainActivity;

public class DeviceItemViewModel extends ViewModel {
    public MutableLiveData<String> vendorId;
    public MutableLiveData<String> productId;
    public MutableLiveData<String> deviceName;
    MainActivity view;

    public DeviceItemViewModel(MainActivity view) {
        this.view = view;
    }

    public void loadItem(DeviceService.DeviceItem item) {
        deviceName.setValue(item.deviceName);
        vendorId.setValue("vendor:" + item.vendorId);
        productId.setValue("product:" + item.productId);
    }

    public void onItemClick(View itemView) {

    }
}
