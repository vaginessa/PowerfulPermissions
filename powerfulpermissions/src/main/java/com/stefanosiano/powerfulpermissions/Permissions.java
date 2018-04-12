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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permissions {

    private static Context appContext;
    private final static SparseArray<PermMapping> permissionMap = new SparseArray<>();

    private final static SparseArray<PermissionHelper> helperArray = new SparseArray<>();

    public static void init(Application application) {
        appContext = application.getApplicationContext();
        permissionMap.clear();
        try {
            Class<?> permissionPPClass = Class.forName("com.stefanosiano.powerfulpermissions.Permissions$$PowerfulPermission");
            permissionPPClass.getDeclaredMethod("init", SparseArray.class).invoke(null, permissionMap);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Unable to initialize the activity-permissions mapping: " + e.getLocalizedMessage());
        }
    }


    public static boolean askPermissions(final int requestCode, final Activity activity, final Runnable onPermissionGranted, final Runnable onPermissionDenied){

        final PermMapping permMapping = permissionMap.get(requestCode);

        if(permMapping == null) throw new RuntimeException("Unable to find permissions!");

        final List<String> permissionsToAsk = new ArrayList<>();
        final List<String> permissionsToRationale = new ArrayList<>();

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
            AlertDialog.Builder rationaleDialog = new AlertDialog.Builder(activity);
            //todo set rationale message(s)!
            rationaleDialog.setTitle("PERMISSION REQUESTED!");
            rationaleDialog.setMessage("Rationale!");
            rationaleDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    permissionsToAsk.addAll(permissionsToRationale);
                    requestPermission(activity, listToArray(permissionsToAsk), permMapping, onPermissionGranted, onPermissionDenied);
                }
            });
            rationaleDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
            rationaleDialog.show();
            return true;
        }
        if(permissionsToAsk.size() > 0) {
            requestPermission(activity, listToArray(permissionsToAsk), permMapping, onPermissionGranted, onPermissionDenied);
            return true;
        }

        return false;
    }


    private static void requestPermission(Activity activity, String[] permissions, PermMapping permMapping, final Runnable onPermissionGranted, final Runnable onPermissionDenied){
        helperArray.put(permMapping.requestCode, new PermissionHelper(permMapping, onPermissionGranted, onPermissionDenied));
        ActivityCompat.requestPermissions(activity, permissions, permMapping.methodId);
    }


    public static boolean onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults){

        PermissionHelper permissionHelper = helperArray.get(requestCode);
        if(permissionHelper == null) return false;
        final PermMapping permMapping = permissionHelper.permMapping;
        if(permMapping == null) return false;

        boolean showOnpermissionGranted = true;
        List<String> permRequested = Arrays.asList(permMapping.permissions);
        List<String> permOptional = Arrays.asList(permMapping.optionalPermissions);

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission

                //permission is needed
                if(permRequested.contains(permission)) {
                    showOnpermissionGranted = false;

                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                    if (showRationale) {
                        // user did NOT check "never ask again": explain why you need the permission and ask if user wants to accept it
                        showRationale(permission, R.string.permission_denied_contacts);
//                        ActivityCompat.requestPermissions(activity, permissions, permMapping.methodId);
                    } else {
                        // user also CHECKED "never ask again": open another dialog explaining again the permission and directing to the app setting
                        permissionHelper.onPermissionDenied.run();
                    }
                }

                //permission is optional
                if(permOptional.contains(permission)) {
                    showOnpermissionGranted = false;

                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                    if (showRationale) {
                        // user did NOT check "never ask again": explain why you need the permission and ask if user wants to accept it
                        showRationale(permission, R.string.permission_denied_contacts);
                    } else {
                        // user also CHECKED "never ask again", but permission is optional, so function can be called anyway
                        permissionHelper.onPermissionGranted.run();
                    }
                }
            }
            else {

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

        List<String> strings = new ArrayList<>();

        for(String s : array1) if(!TextUtils.isEmpty(s) && !strings.contains(s)) strings.add(s);
        for(String s : array2) if(!TextUtils.isEmpty(s) && !strings.contains(s)) strings.add(s);

        return listToArray(strings);
    }
}
