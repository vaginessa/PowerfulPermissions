package com.stefanosiano.powerfulpermissions;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.stefanosiano.powerfulpermissions.annotation.PermMapping;

import java.util.HashMap;
import java.util.Map;

public class Permissions {

    private static Context appContext;
    private final static Map<String, PermMapping> permissionMap = new HashMap<>();

    public static void init(Application application) {
        appContext = application.getApplicationContext();
        permissionMap.clear();
        try {
            Class<?> permissionPPClass = Class.forName("com.stefanosiano.powerfulpermissions.Permissions$$PowerfulPermission");
            permissionPPClass.getDeclaredMethod("init", Map.class).invoke(null, permissionMap);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Unable to initialize the activity-permissions mapping: " + e.getLocalizedMessage());
        }
    }

    public static boolean askPermissions(Object ob) {
    }

    public static boolean askPermissions(int id, Object ob){

//        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        PermMapping permMapping = permissionMap.get(ob.getClass().getName() + "$" + id);

//        if(permMapping == null) throw new RuntimeException("Unable to find permissions for the method " + methodName);
        if(permMapping == null) throw new RuntimeException("Unable to find permissions!");


        for (String perm : permMapping.permissions) {
            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED)
                return true;
        }

        for (String perm : permMapping.optionalPermissions) {

            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED){

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, perm)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(thisActivity,
                            perm,
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                return true;
            }
        }

        return false;
    }



    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Object ob;
        ob.getClass().getMethod("asd", String.class).invoke(ob, "s");
    }

}
