package com.stefanosiano.powerfulpermissions;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public class Permissions {

    private static boolean initialized = false;
    static Map<String, SparseArray<ContextPermMapping>> activitiesMap = new HashMap<>();

    public static void init() {
        activitiesMap = new HashMap<>();
        try {
            Class permissionPPClass = ClassLoader.getSystemClassLoader().loadClass("com.stefanosiano.powerfulpermissions.Permissions$PowerfulPermission");
            permissionPPClass.getDeclaredMethod("init", Map.class).invoke(null, activitiesMap);
            initialized = true;
        }
        catch (Exception e){
            new RuntimeException("Unable to initialize the activity-permissions mapping");
        }
    }

    public static boolean check(Context context){
        initialized = true;
        SparseArray<ContextPermMapping> contextPermMappings = activitiesMap.get(context.getClass().getName());

        if(contextPermMappings == null) throw new RuntimeException("Trying to call a method not annotated");

        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        ContextPermMapping contextPermMapping = ContextPermMappingFinder.findByMethod(methodName, contextPermMappings);

        if(contextPermMapping == null) throw new RuntimeException("Trying to call a method without permissions in annotation");


        for (String perm : contextPermMapping.permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String perm : contextPermMapping.permissions) {
                if (context.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        else
            return true;

        return true;
    }


    public static class ContextPermMappingFinder {
        public static ContextPermMapping findByMethod(String methodName, SparseArray<ContextPermMapping> contextPermMappingSparseArray){
            for(int i = 0 ; i < contextPermMappingSparseArray.size(); i++){
                if(contextPermMappingSparseArray.get(i).methodName.equals(methodName))
                    return contextPermMappingSparseArray.get(i);
            }
            return null;
        }
    }

    public class ContextPermMapping {
        String[] permissions;
        String methodName;
        int methodId;
    }
}
