package com.stefanosiano.powerfulpermissionssample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.stefanosiano.powerfulpermissions.Permissions;
import com.stefanosiano.powerfulpermissions_annotation.Perms;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asd();
    }


    @Perms({"jjj"})
    private void asd(){
        if(!Permissions.check(this)) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    }
/*

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
    }*/
}
