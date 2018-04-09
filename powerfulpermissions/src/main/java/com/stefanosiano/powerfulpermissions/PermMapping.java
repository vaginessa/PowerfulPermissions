package com.stefanosiano.powerfulpermissions;

public class PermMapping {
    public String[] permissions;
    public String[] optionalPermissions;
    public String methodName;
    public int methodId;

    public PermMapping(String[] permissions, String[] optionalPermissions, String methodName, int methodId) {
        this.permissions = permissions;
        this.optionalPermissions = optionalPermissions;
        this.methodName = methodName;
        this.methodId = methodId;
    }
}
