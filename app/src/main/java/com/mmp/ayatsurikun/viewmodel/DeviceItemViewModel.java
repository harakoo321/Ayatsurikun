package com.mmp.ayatsurikun.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.view.MainActivity;

public class DeviceItemViewModel extends ViewModel {
    private MutableLiveData<String> deviceName;
    MainActivity view;

    public DeviceItemViewModel(MainActivity view) {
        this.view = view;
    }
}
