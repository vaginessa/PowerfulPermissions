package com.stefanosiano.powerfulpermissions

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.core.app.ActivityCompat
import com.stefanosiano.powerfulpermissions.Permissions


internal class SimpleOnPermissionDeniedListener : Permissions.PermissionDeniedListener {

    override fun onPermissionsDenied(permissions: Array<String>) {

    }

}

internal class SimpleShowRationaleListener(private val rationaleId: Int) : Permissions.ShowRationaleListener {

    override fun onShowRationale(permissions: Array<String>, activity: Activity, requestCode: Int) {
        val rationaleDialog = AlertDialog.Builder(activity)
        //todo set rationale message(s)!
        rationaleDialog.setTitle("Title!")
        rationaleDialog.setMessage(rationaleId)
        rationaleDialog.setPositiveButton("YES") { dialog, which -> ActivityCompat.requestPermissions(activity, permissions, requestCode) }
        rationaleDialog.setNegativeButton("NO") { dialog, which -> }
        rationaleDialog.show()
    }
}
