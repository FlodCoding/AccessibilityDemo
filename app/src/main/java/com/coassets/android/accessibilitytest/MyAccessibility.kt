package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.gesture.Gesture
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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val gesture = intent.getParcelableExtra<Gesture>("gesture")

        if (gesture != null) {
            val builder = GestureDescription.Builder()
            gesture.strokes[0].points[0]

            val path = gesture.strokes[0].path
            val description =
                GestureDescription.StrokeDescription(path, 100, (gesture.length / 2).toLong())
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

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Log.d("onAccessibilityEvent", event.toString())


        /* val pkgName = event.packageName.toString()
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
         }*/


    }


}