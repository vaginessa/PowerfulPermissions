package com.stefanosiano.powerfulpermissions.simpleListeners;

import com.stefanosiano.powerfulpermissions.Permissions;

public class SimpleShowRationaleListener implements Permissions.ShowRationaleListener {

    private int rationaleId;

    public SimpleShowRationaleListener(int rationaleId) {
        this.rationaleId = rationaleId;
    }

    @Override
    public void onShowRationale(String[] permissions) {

    }
}
