package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_ANNOUNCEMENT


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
       // Log.d("onAccessibilityEvent", event.toString())
        event ?: return


        val pkgName = event.packageName.toString()
        val className = event.className.toString()

        


        if (event.eventType == TYPE_ANNOUNCEMENT
            && pkgName == "com.android.systemui"
            && className == "android.view.View"
        ) {
            Log.d("MyAccessibility", "我进来了")

            val path = Path()
            path.moveTo(250f, 1400f)
            path.lineTo(800f, 1400f)
            val builder = GestureDescription.Builder()
            builder.addStroke(GestureDescription.StrokeDescription(path, 100, 700))
            dispatchGesture(builder.build(), object : GestureResultCallback() {
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    Log.d("MyAccessibility", "onCancelled")
                }

                override fun onCompleted(gestureDescription: GestureDescription?) {
                    Log.d("MyAccessibility", "onCompleted")
                }
            }, null)
        }






    }



}