package com.stefanosiano.powerfulpermissionssample;

import android.app.Application;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Permissions.init(this);
    }
}
