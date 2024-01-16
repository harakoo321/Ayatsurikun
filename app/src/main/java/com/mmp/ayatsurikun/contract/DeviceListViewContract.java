package com.mmp.ayatsurikun.contract;

import com.mmp.ayatsurikun.model.ConnectionMethod;
import com.mmp.ayatsurikun.model.Device;

import java.util.List;

public interface DeviceListViewContract {
    void showDevices(List<Device> devices);
    void startSignalButtonsActivity(String deviceId, int port, ConnectionMethod connectionMethod);
}
