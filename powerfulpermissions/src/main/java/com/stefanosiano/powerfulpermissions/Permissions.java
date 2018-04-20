package com.stefanosiano.powerfulpermissions;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;

import com.stefanosiano.powerfulpermissions.simpleListeners.SimpleOnPermissionDeniedListener;
import com.stefanosiano.powerfulpermissions.simpleListeners.SimpleShowRationaleListener;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Permissions {

    private static Context appContext;
    private final static HashMap<String, PermissionsHelper> permissionHelperMap = new HashMap<>();
//    private final static SparseArray<PermMapping> permissionMap = new SparseArray<>();
//    private final static SparseArray<PermissionHelper> helperArray = new SparseArray<>();


    public static void init(Application application) {
        appContext = application.getApplicationContext();
        PermissionsHelper.init(appContext);
        permissionMap.clear();
        try {
            Class<?> permissionPPClass = Class.forName("com.stefanosiano.powerfulpermissions.Permissions$$PowerfulPermission");
            permissionPPClass.getDeclaredMethod("init", SparseArray.class).invoke(null, permissionMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to initialize the activity-permissions mapping: " + e.getLocalizedMessage());
        }
    }


    public static PermissionsHelper with(int requestCode, Activity activity) {
        String key = requestCode+activity.getClass().getName();
        if(permissionHelperMap.get(key) == null) permissionHelperMap.put(key, new PermissionsHelper(requestCode));
        return permissionHelperMap.get(key);
    }

    public static boolean onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        String key = requestCode+activity.getClass().getName();
        PermissionsHelper permissionsHelper = permissionHelperMap.get(key);
        if(permissionsHelper == null)
            return false;

        return permissionsHelper.onRequestPermissionsResult(activity, permissions, grantResults);
    }


    public interface PermissionDeniedListener {
        void onPermissionsDenied(String[] permissions);
    }


    public interface ShowRationaleListener {
        void onShowRationale(String[] permissions, Activity activity, int requestCode);
    }
}
