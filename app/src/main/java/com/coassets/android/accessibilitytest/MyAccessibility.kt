package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.coassets.android.accessibilitytest.gesture.GestureInfo


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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val gesture = intent.getParcelableArrayListExtra<GestureInfo>("gesture")
        startGesture(gesture)
        return super.onStartCommand(intent, flags, startId)
    }


    fun startGesture(list: ArrayList<GestureInfo>) {
        val builder = GestureDescription.Builder()
        val description =
            GestureDescription.StrokeDescription(Path(), 0, 10)

        for (gestureInfo in list) {
            val gesture = gestureInfo.gesture

            gesture.toPath()
            val path = gesture.toPath()

            var duration = gestureInfo.duration
            if (duration > 60000) {
                duration = 60000
            }
            description.continueStroke(path, gestureInfo.delayTime, duration, false)
        }




        builder.addStroke(description)

        dispatchGesture(builder.build(), object : GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.d("MyAccessibility", "onCancelled")
            }

            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d("MyAccessibility", "onCompleted")
            }
        }, null)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {


    }


}