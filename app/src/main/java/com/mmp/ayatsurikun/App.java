package com.mmp.ayatsurikun;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.setApplicationContext(this);
    }

    public static class ContextProvider {
        private static Context applicationContext;

        private static void setApplicationContext(Context context) {
            applicationContext = context;
        }

        public static Context getApplicationContext() {
            return applicationContext;
        }
    }
}
