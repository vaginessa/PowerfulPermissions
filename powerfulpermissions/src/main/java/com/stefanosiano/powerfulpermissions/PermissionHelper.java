package com.stefanosiano.powerfulpermissions;

public class PermissionHelper {
    PermMapping permMapping;
    Runnable onPermissionGranted;
    Permissions.PermissionDeniedListener onPermissionDenied;

    public PermissionHelper(PermMapping permMapping, Runnable onPermissionGranted, Permissions.PermissionDeniedListener onPermissionDenied) {
        this.permMapping = permMapping;
        this.onPermissionGranted = onPermissionGranted;
        this.onPermissionDenied = onPermissionDenied;
    }
}
