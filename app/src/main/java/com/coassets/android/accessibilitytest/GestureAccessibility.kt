package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.Intent
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
class GestureAccessibility : AccessibilityService() {
    private var mGestures: ArrayList<GestureInfo> = ArrayList()
    private var mIndex = 0

    companion object {

        fun startGestures(context: Context, gestures: ArrayList<GestureInfo>) {
            val intent = Intent(context, GestureAccessibility::class.java)
            intent.putParcelableArrayListExtra("gesture", gestures)
            intent.putExtra("isList", true)
            context.startService(intent)
        }

        fun startGesture(context: Context, gestures: GestureInfo) {
            val intent = Intent(context, GestureAccessibility::class.java)
            intent.putExtra("isList", false)
            intent.putExtra("gesture", gestures)
            context.startService(intent)
        }
    }


    override fun onInterrupt() {

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val isList = intent.getBooleanExtra("isList", false)
        if (isList) {
            mGestures = intent.getParcelableArrayListExtra("gesture")
        } else {
            val gesture = intent.getParcelableExtra<GestureInfo>("gesture")
            mGestures.clear()
            mGestures.add(gesture)
        }
        
        startGesture(mGestures[mIndex])
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startGesture(gestureInfo: GestureInfo) {
        val builder = GestureDescription.Builder()

        val gesture = gestureInfo.gesture

        var duration = gestureInfo.duration
        if (duration > GestureDescription.getMaxGestureDuration()) {
            duration = GestureDescription.getMaxGestureDuration()
        }


        for (stroke in gesture.strokes.withIndex()) {
            if (stroke.index == GestureDescription.getMaxStrokeCount() - 1)
                break
            val description =
                GestureDescription.StrokeDescription(
                    stroke.value.path,
                    gestureInfo.delayTime,
                    duration
                )

            builder.addStroke(description)
        }


        dispatchGesture(builder.build(), object : GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.d("GestureAccessibility", "onCancelled")
            }

            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d("GestureAccessibility", "onCompleted")
                mIndex++
                if (mGestures.size > mIndex) {
                    startGesture(mGestures[mIndex])
                } else {
                    mIndex = 0
                }

            }
        }, null)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {


    }


}