package com.coassets.android.accessibilitytest.floating

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult



/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-08-27
 * UseDes:
 */
object FloatingWindowsUtil {
    fun startFloatingService(activity: Activity) {

        if (Settings.canDrawOverlays(activity)) {
            activity.startService(Intent(activity, FloatingService::class.java))
        } else {
            Toast.makeText(activity, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
            startActivityForResult(
                activity,
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.packageName)
                ), 0, null
            )


        }
    }
}
