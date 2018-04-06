package com.stefanosiano.powerfulpermissionssample;

import android.app.Application;

import com.stefanosiano.powerfulpermissions.Permissions;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Permissions.init();
    }
}
