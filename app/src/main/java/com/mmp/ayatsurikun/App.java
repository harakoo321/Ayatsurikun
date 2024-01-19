package com.mmp.ayatsurikun;

import android.app.Application;
import android.content.Context;

import com.mmp.ayatsurikun.model.Device;

//@HiltAndroidApp
public class App extends Application {
    private Device device;
    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.setApplicationContext(getApplicationContext());
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static class ContextProvider {
        private static Context applicationContext;

        private static void setApplicationContext(Context context) {
            applicationContext = context;
        }

        public static Context getContext() {
            return applicationContext;
        }
    }
}
