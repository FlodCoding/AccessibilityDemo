package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityEvent.TYPE_ANNOUNCEMENT
import android.view.accessibility.AccessibilityNodeInfo


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
        //Log.d("onAccessibilityEvent", event.toString())
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


        /*if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED &&
            pkgName.equals("com.qmuiteam.qmuidemo") &&
            className.equals("android.support.v7.widget.RecyclerView")
        ) {
            Log.d("MyAccessibility", "我进来了")

             val path = Path()
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

        /* val view = rootInActiveWindow.findAccessibilityNodeInfosByText("header 1")
         if (view.isNotEmpty()){
             val bounds = Rect()

             view[0].getBoundsInScreen(bounds)


             val builder = GestureDescription.Builder()
             val  path = Path()
             path.moveTo(bounds.centerX().toFloat(), bounds.centerY().toFloat())
             builder.addStroke(GestureDescription.StrokeDescription(path, 100, 700))
             dispatchGesture(builder.build(), object : GestureResultCallback() {
                 override fun onCancelled(gestureDescription: GestureDescription?) {
                     Log.d("MyAccessibility", "onCancelled")
                 }

                 override fun onCompleted(gestureDescription: GestureDescription?) {
                     Log.d("MyAccessibility", "onCompleted")
                 }
             }, null)

             //click(bounds.centerX(),bounds.centerY())

         }*/

        // view[0].performAction(AccessibilityNodeInfo.ACTION_CLICK)


    }


    private fun click(x: Int, y: Int) {

        clickAtPosition(x, y, rootInActiveWindow)
    }

    private fun clickAtPosition(x: Int, y: Int, node: AccessibilityNodeInfo?) {
        if (node == null) return

        if (node.childCount == 0) {
            val buttonRect = Rect()
            node.getBoundsInScreen(buttonRect)
            if (buttonRect.contains(x, y)) {
                // Maybe we need to think if a large view covers item?
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                println("1º - Node Information: $node")
            }
        } else {
            val buttonRect = Rect()
            node.getBoundsInScreen(buttonRect)
            if (buttonRect.contains(x, y)) {
                // Maybe we need to think if a large view covers item?
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                println("2º - Node Information: $node")
            }
            for (i in 0 until node.childCount) {
                clickAtPosition(x, y, node.getChild(i))
            }
        }
    }
}