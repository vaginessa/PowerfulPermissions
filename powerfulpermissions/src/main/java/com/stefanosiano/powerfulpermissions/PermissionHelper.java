package com.stefanosiano.powerfulpermissions;

public class PermissionHelper {
    PermMapping permMapping;
    Runnable onPermissionGranted;
    Permissions.PermissionDeniedListener onPermissionDenied;
    Permissions.ShowRationaleListener onShowRationale;

    public PermissionHelper(PermMapping permMapping, Runnable onPermissionGranted, Permissions.PermissionDeniedListener onPermissionDenied, Permissions.ShowRationaleListener onShowRationale) {
        this.permMapping = permMapping;
        this.onPermissionGranted = onPermissionGranted;
        this.onPermissionDenied = onPermissionDenied;
        this.onShowRationale = onShowRationale;
    }
}
