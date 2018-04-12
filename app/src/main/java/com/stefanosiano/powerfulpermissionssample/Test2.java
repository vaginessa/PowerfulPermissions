package com.stefanosiano.powerfulpermissionssample;

import android.Manifest;

import com.stefanosiano.powerfulpermissions.annotation.RequiresPermissions;


public class Test2 {

    @RequiresPermissions(requestCode = 21, required = {Manifest.permission.ACCESS_CHECKIN_PROPERTIES, Manifest.permission.ACCESS_FINE_LOCATION})
    public void a2e3(){}
}
