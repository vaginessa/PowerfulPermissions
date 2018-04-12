package com.stefanosiano.powerfulpermissionssample;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.stefanosiano.powerfulpermissions.Permissions;
import com.stefanosiano.powerfulpermissions.annotation.RequiresPermissions;

public class MainActivity extends AppCompatActivity {

    public static final String[] p = {Manifest.permission.ACCESS_CHECKIN_PROPERTIES, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFile(Environment.DIRECTORY_DOWNLOADS);
    }


    @RequiresPermissions(requestCode = 2, required = Manifest.permission.READ_EXTERNAL_STORAGE)
    private void readFile(String path){
        if(Permissions.askPermissions(2, this, ()->readFile(path), this::onReadFilePermissionDenied)) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: " + path);
    }

    private void onReadFilePermissionDenied(){
        Log.e("ASD", "NUOOOOOOOOOOOOOOOOOOO");
    }


    @RequiresPermissions(requestCode = 1, required = Manifest.permission.ACCESS_COARSE_LOCATION)
    private void asd(){
        if(Permissions.askPermissions(1, this, this::asd, this::asd)) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    }


    @RequiresPermissions(requestCode = 3, required = {"jhjj"}, optional = "")
    private void asd2(){
        if(Permissions.askPermissions(3, this,  this::asd2, this::asd2)) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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
