package com.stefanosiano.powerfulpermissions;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

    public static boolean askPermissions(int id, Object ob){
        return askPermissions(id, null, ob, null);
    }
    public static boolean askPermissions(int id, Activity activity, Object ob, Runnable onPermissionDenied){

        PermMapping permMapping = permissionMap.get(ob.getClass().getName() + "$" + id);

        if(permMapping == null) throw new RuntimeException("Unable to find permissions!");

        List<String> permissionsToAsk = new ArrayList<>();
        List<String> permissionsToRationale = new ArrayList<>();

        String[] permissions = mergeArrays(permMapping.permissions, permMapping.optionalPermissions);

        for (String perm : permissions) {

            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED){

                // Permission is not granted. Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm))
                    permissionsToRationale.add(perm);
                // No explanation needed; request the permission
                else
                    permissionsToAsk.add(perm);
            }
        }

        if(permissionsToRationale.size() > 0){
            //todo show rationale!
            return true;
        }
        if(permissionsToAsk.size() > 0) {
            ActivityCompat.requestPermissions(activity, listToArray(permissionsToAsk), id);
            return true;
        }

        return false;
    }



    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults){
        Object ob;
        ob.getClass().getMethod("asd", String.class).invoke(ob, "s");

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                if (!showRationale) {
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
                } else if ( /* possibly check more permissions...*/) {
                }
            }
        }
    }


    private static String[] listToArray(List<String> list){
        if(list == null)
            return null;
        String[] strings = new String[list.size()];
        for(int i = 0; i < strings.length; i++)
            strings[i] = list.get(i);
        return strings;
    }


    private static String[] mergeArrays(String[] array1, String[] array2){
        if(array1 == null && array2 == null)
            return null;

        if(array1 == null) array1 = new String[0];
        if(array2 == null) array2 = new String[0];

        int max = array1.length + array2.length;

        String[] strings = new String[max];

        System.arraycopy(array1, 0, strings, 0, array1.length);
        System.arraycopy(array2, 0, strings, array1.length, array1.length+array2.length);

        return strings;
    }
}
