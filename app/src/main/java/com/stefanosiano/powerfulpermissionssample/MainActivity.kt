package com.stefanosiano.powerfulpermissionssample

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.stefanosiano.powerfulpermissions.Permissions
import com.stefanosiano.powerfulpermissions.annotation.RequiresPermissions


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readFile(Environment.DIRECTORY_DOWNLOADS)
    }


    //    @RequiresPermissions(requestCode = 2, required = p)
    private fun readFile(path: String) {

        if (Permissions.with(this, 2)
                        .onDenied { onReadFilePermissionDenied(it) }
                        .askPermissions(p, R.string.app_name) { readFile(path) })
            return

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: $path")
    }

    private fun onReadFilePermissionDenied(deniedPermissions: Array<String>) {
        Log.e("ASD", "NUOOOOOOOOOOOOOOOOOOO")
    }


    @RequiresPermissions(requestCode = 1, required = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
    private fun asd() {
        if (Permissions.with(this, 2)
                        .onDenied { this.onReadFilePermissionDenied(it) }
                        .askPermissions(p, R.string.app_name) { this.asd() })
        return

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
    }


    @RequiresPermissions(requestCode = 3, required = arrayOf("jhjj"), optional = arrayOf(""))
    private fun asd2() {
        //        if(Permissions.askPermissions(3, this, (permissions -> {}), this::asd2)) return;

        Log.e("ASD", "Yeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Permissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) return
    }

    companion object {

        val p = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val p2 = Manifest.permission.ACCESS_CHECKIN_PROPERTIES
    }
}
