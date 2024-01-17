package com.mmp.ayatsurikun;

import android.app.Application;
import android.content.Context;

//@HiltAndroidApp
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.setApplicationContext(getApplicationContext());
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
