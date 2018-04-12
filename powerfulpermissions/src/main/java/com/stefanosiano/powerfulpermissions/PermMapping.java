package com.stefanosiano.powerfulpermissions;

public class PermMapping {
    public String[] permissions;
    public String[] optionalPermissions;
    public String methodName;
    public int requestCode;
    public int methodId;

    public PermMapping(String[] permissions, String[] optionalPermissions, String methodName, int requestCode, int methodId) {
        this.permissions = permissions;
        this.optionalPermissions = optionalPermissions;
        this.methodName = methodName;
        this.requestCode = requestCode;
        this.methodId = methodId;
    }
}
