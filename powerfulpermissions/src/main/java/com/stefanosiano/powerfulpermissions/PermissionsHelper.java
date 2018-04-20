package com.stefanosiano.powerfulpermissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.stefanosiano.powerfulpermissions.simpleListeners.SimpleOnPermissionDeniedListener;
import com.stefanosiano.powerfulpermissions.simpleListeners.SimpleShowRationaleListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionsHelper {

    private static Context appContext;
    private final int requestCode;
    private final WeakReference<Activity> activityReference;
    private String[] optionalPermissions;
    private Permissions.PermissionDeniedListener onPermissionDenied;
    private Permissions.ShowRationaleListener showRationaleListener;
    private Runnable onGranted;


    static void init(Context appContext){
        PermissionsHelper.appContext = appContext.getApplicationContext();
    }


    PermissionsHelper(int requestCode, Activity activity) {
        this.requestCode = requestCode;
        this.activityReference = new WeakReference<>(activity);
        onPermissionDenied = new SimpleOnPermissionDeniedListener();
    }

    public PermissionsHelper optionalPermissions(String[] optionalPermissions) {
        this.optionalPermissions = optionalPermissions;
        return this;
    }

    public PermissionsHelper onDenied(Permissions.PermissionDeniedListener onPermissionDenied) {
        this.onPermissionDenied = onPermissionDenied;
        return this;
    }

    public boolean askPermissions(String[] permissions, final int rationaleId, final Runnable onPermissionGranted) {
        return askPermissions(permissions, new SimpleShowRationaleListener(rationaleId), onPermissionGranted);
    }

    public boolean askPermissions(String[] permissions, final Permissions.ShowRationaleListener showRationaleListener, final Runnable onPermissionGranted) {

        this.showRationaleListener = showRationaleListener;
        this.onGranted = onPermissionGranted;

        final Activity activity = activityReference.get();
        if(activity == null)
            return true;

        final List<String> permissionsToAsk = new ArrayList<>();
        final List<String> permissionsToRationale = new ArrayList<>();

        for (String perm : permissions) {

            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted. Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm))
                    permissionsToRationale.add(perm);
                    // No explanation needed; request the permission
                else
                    permissionsToAsk.add(perm);
            }
        }

        if (permissionsToRationale.size() > 0) {
            permissionsToRationale.addAll(permissionsToAsk);
            showRationaleListener.onShowRationale(listToArray(permissionsToRationale), activity, requestCode);
            return true;
        }

        if (permissionsToAsk.size() > 0) {
            ActivityCompat.requestPermissions(activity, listToArray(permissionsToAsk), requestCode);
            return true;
        }

        return false;
    }




    public boolean onRequestPermissionsResult(Activity activity, String[] permissions, int[] grantResults) {

        boolean shouldShowRationale = false;
        boolean shouldRunDenied = false;

        List<String> permOptional = Arrays.asList(optionalPermissions);

        List<String> deniedPermissions = new ArrayList<>();
        List<String> rationalePermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission

                //permission is optional
                if (permOptional.contains(permission)) {

                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                    if (showRationale) {
                        // user did NOT check "never ask again": explain why you need the permission and ask if user wants to accept it
                        shouldShowRationale = true;
                        rationalePermissions.add(permission);
                    } else {
                        // user also CHECKED "never ask again", but permission is optional, so function can be called anyway
                    }
                }
                //permission is needed
                else {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                    if (showRationale) {
                        // user did NOT check "never ask again": explain why you need the permission and ask if user wants to accept it
                        shouldShowRationale = true;
                        rationalePermissions.add(permission);
                    } else {
                        // user also CHECKED "never ask again": open another dialog explaining again the permission and directing to the app setting
                        shouldRunDenied = true;
                        deniedPermissions.add(permission);
                    }
                }
            }
        }

        deniedPermissions.addAll(rationalePermissions);
        if (shouldRunDenied) {
            onPermissionDenied.onPermissionsDenied(listToArray(deniedPermissions));
            return true;
        }
        if (shouldShowRationale) {
            showRationaleListener.onShowRationale(listToArray(rationalePermissions), activity, requestCode);
            return true;
        }

        onGranted.run();
        return true;
    }


    private static String[] listToArray(List<String> list) {
        if (list == null)
            return null;
        String[] strings = new String[list.size()];
        for (int i = 0; i < strings.length; i++)
            strings[i] = list.get(i);
        return strings;
    }


    private static String[] mergeArrays(String[] array1, String[] array2) {
        if (array1 == null && array2 == null)
            return null;

        if (array1 == null) array1 = new String[0];
        if (array2 == null) array2 = new String[0];

        List<String> strings = new ArrayList<>();

        for (String s : array1)
            if (!TextUtils.isEmpty(s) && !strings.contains(s)) strings.add(s);
        for (String s : array2)
            if (!TextUtils.isEmpty(s) && !strings.contains(s)) strings.add(s);

        return listToArray(strings);
    }
}
