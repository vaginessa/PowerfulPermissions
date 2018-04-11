package com.stefanosiano.powerfulpermissions;

public class PermissionHelper {
    PermMapping permMapping;
    Runnable onPermissionGranted, onPermissionDenied;

    public PermissionHelper(PermMapping permMapping, Runnable onPermissionGranted, Runnable onPermissionDenied) {
        this.permMapping = permMapping;
        this.onPermissionGranted = onPermissionGranted;
        this.onPermissionDenied = onPermissionDenied;
    }
}
