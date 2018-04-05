package com.stefanosiano.powerfulpermissionssample;

import java.util.Map;

public class test {

    public static void init(Map map){
        map.clear();
        String[] permissions = {"",""};
        map.put("com.stefanosiano.powerfulpermissionssample.MainActivity",
                new ContextPermMapping(permissions, "asd", 1));
    }

    public static class ContextPermMapping {
        String[] permissions;
        String methodName;
        int methodId;

        public ContextPermMapping(String[] permissions, String methodName, int methodId) {
            this.permissions = permissions;
            this.methodName = methodName;
            this.methodId = methodId;
        }
    }
}
