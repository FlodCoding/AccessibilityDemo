package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent


/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-08-26
 * UseDes:
 *
 */
class MyAccessibility : AccessibilityService() {
    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //Log.d("MyAccessibility", event.toString())
        event ?: return

        val pkgName = event.packageName.toString()
        val className = event.className.toString()
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED &&
            pkgName.equals("com.qmuiteam.qmuidemo") &&
            className.equals("android.support.v7.widget.RecyclerView")
        ) {
            Log.d("MyAccessibility", "我进来了")

            /* val path = Path()
             path.moveTo(500f, 1000f)
             path.lineTo(500f, 500f)


             val builder = GestureDescription.Builder()
             builder.addStroke(GestureDescription.StrokeDescription(path, 100, 700))


             dispatchGesture(builder.build(), object : GestureResultCallback() {
                 override fun onCancelled(gestureDescription: GestureDescription?) {
                     Log.d("MyAccessibility", "onCancelled")
                 }

                 override fun onCompleted(gestureDescription: GestureDescription?) {
                     Log.d("MyAccessibility", "onCompleted")
                 }
             }, null)*/

            val view = rootInActiveWindow.findAccessibilityNodeInfosByText("header 1")
            //view[0].
            // view[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }

    }
}