package com.stefanosiano.powerfulpermissionssample;

import android.Manifest;

import com.stefanosiano.powerfulpermissionsAnnotation.Perms;

public class Test1 {

    @Perms({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void a1(){}

    @Perms({Manifest.permission.READ_PHONE_STATE})
    public void a2(){}

    @Perms({Manifest.permission.ACCESS_CHECKIN_PROPERTIES, Manifest.permission.ACCESS_FINE_LOCATION})
    public void a3(){}
}
