package com.stefanosiano.powerfulpermissions.simpleListeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

import com.stefanosiano.powerfulpermissions.Permissions;

public class SimpleShowRationaleListener implements Permissions.ShowRationaleListener {

    private int rationaleId;

    public SimpleShowRationaleListener(int rationaleId) {
        this.rationaleId = rationaleId;
    }

    @Override
    public void onShowRationale(final String[] permissions, final Activity activity, final int requestCode) {
        AlertDialog.Builder rationaleDialog = new AlertDialog.Builder(activity);
        //todo set rationale message(s)!
        rationaleDialog.setTitle("Title!");
        rationaleDialog.setMessage(rationaleId);
        rationaleDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        });
        rationaleDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        rationaleDialog.show();
    }
}
