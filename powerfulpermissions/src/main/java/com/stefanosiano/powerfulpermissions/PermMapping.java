package com.stefanosiano.powerfulpermissions;

public class PermMapping {
    public String[] permissions;
    public String methodName;
    public int methodId;

    public PermMapping(String[] permissions, String methodName, int methodId) {
        this.permissions = permissions;
        this.methodName = methodName;
        this.methodId = methodId;
    }
}
