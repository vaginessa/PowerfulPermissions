package com.stefanosiano.powerfulpermissions;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.stefanosiano.powerfulpermissions.annotation.PermMapping;

import java.util.HashMap;
import java.util.Map;

public class Permissions {

    private static Context appContext;
    private final static Map<String, PermMapping> permissionMap = new HashMap<>();

    public static void init(Context context) {
        appContext = context.getApplicationContext();
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

    public static boolean askPermissions(Object ob){

        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        PermMapping permMapping = permissionMap.get(ob.getClass().getName() + "$" + methodName);

        if(permMapping == null) throw new RuntimeException("Unable to find permissions for the method " + methodName);


        for (String perm : permMapping.permissions) {
            if (ContextCompat.checkSelfPermission(appContext, perm) != PackageManager.PERMISSION_GRANTED)
                return true;
        }

        return false;
    }

}
