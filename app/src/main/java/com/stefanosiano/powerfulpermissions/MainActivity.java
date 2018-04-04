package com.stefanosiano.powerfulpermissions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Perms
    private void asd(){
        if(!Permissions.check()) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
            startPaddy();
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSIONS)
    public String readFile (String path){
        if (!EasyPermissions.hasPermissions(this, * PERMISSIONS)) {
            EasyPermissions.requestPermissions(this, "meeee e dai!", REQUEST_CODE_PERMISSIONS, * PERMISSIONS)
            return null
        }
    }
}
