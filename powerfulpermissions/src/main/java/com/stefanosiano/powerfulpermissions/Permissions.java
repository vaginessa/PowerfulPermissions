package com.stefanosiano.powerfulpermissions;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.stefanosiano.powerfulpermissions.PermMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        return askPermissions(0, ob);
    }

    public static boolean askPermissions(int id, Object ob){
        return askPermissions(id, null, ob, null);
    }
    public static boolean askPermissions(int id, Activity activity, Object ob, Runnable onPermissionDenied){

//        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        PermMapping permMapping = permissionMap.get(ob.getClass().getName() + "$" + id);

//        if(permMapping == null) throw new RuntimeException("Unable to find permissions for the method " + methodName);
        if(permMapping == null) throw new RuntimeException("Unable to find permissions!");


        List<String> permissionsToAsk = new ArrayList<>();

        for (String perm : permMapping.permissions) {

            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED){

                // Permission is not granted. Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {

                } else {
                    permissionsToAsk.add(perm);
                    // No explanation needed; request the permission
                }
                return true;
            }

            return true;
        }

        for (String perm : permMapping.optionalPermissions) {

            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED){

                // Permission is not granted. Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {

                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(activity, perm, id);
                }
                return true;
            }

        }

        String[] permissionsToAskArray = new String[permissionsToAsk.size()];
        for(int i = 0; i < permissionsToAskArray.length; i++)
            permissionsToAskArray[i] = permissionsToAsk.get(i);
        ActivityCompat.requestPermissions(activity, permissionsToAskArray, id);

        return false;
    }



    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Object ob;
        ob.getClass().getMethod("asd", String.class).invoke(ob, "s");

        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
            // user rejected the permission
            boolean showRationale = shouldShowRequestPermissionRationale( permission );
            if (! showRationale) {
                // user also CHECKED "never ask again"
                // you can either enable some fall back,
                // disable features of your app
                // or open another dialog explaining
                // again the permission and directing to
                // the app setting
            } else if (Manifest.permission.WRITE_CONTACTS.equals(permission)) {
                showRationale(permission, R.string.permission_denied_contacts);
                // user did NOT check "never ask again"
                // this is a good place to explain the user
                // why you need the permission and ask if he wants
                // to accept it (the rationale)
            } else if ( /* possibly check more permissions...*/ ) {
            }
        }
    }

}
