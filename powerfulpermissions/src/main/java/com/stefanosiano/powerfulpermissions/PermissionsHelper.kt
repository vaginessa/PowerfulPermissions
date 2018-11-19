package com.stefanosiano.powerfulpermissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import java.util.*

object Permissions {

    private val permissionHelperMap = HashMap<String, PermissionsHelper>()


    fun init() = permissionHelperMap.clear()

    fun with(activity: Activity, requestCode: Int): PermissionsHelper {
        val key = requestCode.toString() + activity.javaClass.name
        permissionHelperMap.remove(key)
        permissionHelperMap[key] = PermissionsHelper(requestCode, activity)
        return permissionHelperMap[key]!!
    }

    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
        val key = requestCode.toString() + activity.javaClass.name
        val permissionsHelper = permissionHelperMap[key] ?: return false

        permissionHelperMap.remove(key)
        return permissionsHelper.onRequestPermissionsResult(activity, permissions, grantResults)
    }



    interface ShowRationaleListener {
        fun onShowRationale(permissions: Array<String>, activity: Activity, requestCode: Int)
    }
}


class PermissionsHelper internal constructor(private val requestCode: Int, activity: Activity) {


    private val activityReference = WeakReference(activity)
    private var optionalPermissions: Array<String>? = null
    private var onPermissionDenied: (perms: Array<String>) -> Unit = {  }
    private var showRationaleListener: Permissions.ShowRationaleListener = object : Permissions.ShowRationaleListener { override fun onShowRationale(permissions: Array<String>, activity: Activity, requestCode: Int){}}
    private var onGranted: () -> Unit = {  }


    fun optionalPermissions(optionalPermissions: Array<String>): PermissionsHelper {
        this.optionalPermissions = optionalPermissions
        return this
    }

    fun onDenied(onPermissionDenied: (perms: Array<String>) -> Unit): PermissionsHelper {
        this.onPermissionDenied = onPermissionDenied
        return this
    }

    fun askPermissions(permissions: Array<String>, rationaleId: Int, onPermissionGranted: () -> Unit): Boolean = askPermissions(permissions, SimpleShowRationaleListener(rationaleId), onPermissionGranted)


    fun askPermissions(permissions: Array<String>, showRationaleListener: Permissions.ShowRationaleListener, onPermissionGranted: () -> Unit): Boolean {

        this.showRationaleListener = showRationaleListener
        this.onGranted = onPermissionGranted

        val activity = activityReference.get() ?: return true

        val permissionsToAsk = ArrayList<String>()
        val permissionsToRationale = ArrayList<String>()


        permissions.filter { ContextCompat.checkSelfPermission(activity.applicationContext, it) != PackageManager.PERMISSION_GRANTED }
                .forEach {
                    // Permission is not granted. Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) permissionsToRationale.add(it)
                    else permissionsToAsk.add(it)// No explanation needed; request the permission
                }


        if (permissionsToRationale.isNotEmpty()) {
            permissionsToRationale.addAll(permissionsToAsk)
            showRationaleListener.onShowRationale(permissionsToRationale.toTypedArray(), activity, requestCode)
            return true
        }

        if (permissionsToAsk.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToAsk.toTypedArray(), requestCode)
            return true
        }

        return false
    }


    internal fun onRequestPermissionsResult(activity: Activity, permissions: Array<String>, grantResults: IntArray): Boolean {

        var shouldShowRationale = false
        var shouldRunDenied = false

        val permOptional = optionalPermissions?.asList() ?: ArrayList()

        val deniedPermissions = ArrayList<String>()
        val rationalePermissions = ArrayList<String>()

        for (i in permissions.indices) {
            val permission = permissions[i]

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission

                val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

                //if at least 1 permission needs rationale, I must show it!
                shouldShowRationale = shouldShowRationale.or(showRationale)

                // permission is optional or needed. user did NOT check "never ask again": show rationale and ask it
                if (showRationale) rationalePermissions.add(permission)

                //if permission is optional and (!showRationale) -> user CHECKED "never ask again", but permission is optional, so function can be called anyway

                //if permission is needed
                if (!permOptional.contains(permission)) {
                    // user CHECKED "never ask again": open another dialog explaining again the permission and directing to the app setting
                    if (!showRationale)  {
                        shouldRunDenied = true
                        deniedPermissions.add(permission)
                    }
                }
            }
        }

        deniedPermissions.addAll(rationalePermissions)

        if (shouldRunDenied) {
            onPermissionDenied.invoke(deniedPermissions.toTypedArray())
            return true
        }
        if (shouldShowRationale) {
            showRationaleListener.onShowRationale(rationalePermissions.toTypedArray(), activity, requestCode)
            return true
        }

        onGranted.invoke()
        return true
    }
}
