package com.coassets.android.accessibilitytest

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
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
class GestureAccessibility : AccessibilityService(), GestureRecordService.OnGestureRecordServiceListener {

    companion object {
        private const val KEY_IS_RECORD = "KEY_IS_RECORD"

        fun startGestures(context: Context, gestures: ArrayList<GestureInfo>) {
            val intent = Intent(context, GestureAccessibility::class.java)
            intent.putParcelableArrayListExtra("gesture", gestures)
            context.startService(intent)
        }

        fun startServiceWithRecord(context: Context) {
            val intent = Intent(context, GestureAccessibility::class.java)
            intent.putExtra(KEY_IS_RECORD, true)
            context.startService(intent)
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("GestureAccessibility", "onStartCommand")
        val isRecord = intent.getBooleanExtra(KEY_IS_RECORD, false)
        if (!isRecord) {
            val gestures = intent.getParcelableArrayListExtra<GestureInfo>("gesture")
            if (gestures != null) {
                dispatchGestures(gestures)
            }
        } else {
            //RecordGesture
            bindGestureRecordService()
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun dispatchGestures(gestures: ArrayList<GestureInfo>) {

        var index = 0
        val callBack = object : GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.d("GestureAccessibility", "onCancelled")
            }

            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d("GestureAccessibility", "onCompleted")

                index++

                if (gestures.size > index) {
                    startGesture(gestures[index], this, false)
                } else {
                    index = 0
                }

            }
        }

        for (gesture in gestures) {
            startGesture(gesture, callBack, false)
        }
    }


    private fun startGesture(gestureInfo: GestureInfo, gestureResultCallback: GestureResultCallback, immediately: Boolean) {
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
                    if (immediately) 0 else gestureInfo.delayTime,
                    duration
                )

            builder.addStroke(description)
        }

        dispatchGesture(builder.build(), gestureResultCallback, null)
    }


    //=============================== RecordService Part===========================================//
    private var mRecordServiceBinder: GestureRecordService.IGestureRecordBinder? = null
    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("GestureAccessibility", "onServiceDisconnected")
            mRecordServiceBinder?.setOnGestureRecordedListener(null)
            mRecordServiceBinder = null


        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("GestureAccessibility", "onServiceConnected")
            if (service is GestureRecordService.IGestureRecordBinder) {
                service.setOnGestureRecordedListener(this@GestureAccessibility)
                mRecordServiceBinder = service

            }

        }
    }

    private fun bindGestureRecordService() {
        bindService(
            Intent(this, GestureRecordService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }


    //==========================OnGestureRecordServiceListener==================================//


    override fun onStartRecord() {

    }


    override fun onRecorded(gestureInfo: GestureInfo) {
        startGesture(gestureInfo, object : GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                mRecordServiceBinder?.onResult(true)
            }

            override fun onCompleted(gestureDescription: GestureDescription?) {
                mRecordServiceBinder?.onResult(false)
            }
        }, true)
    }

    override fun onStopRecord() {

    }


    override fun onUnbindRecordService() {
        unbindService(mServiceConnection)
    }

    //=======================================================================================//


    override fun onInterrupt() {


    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {


    }




}