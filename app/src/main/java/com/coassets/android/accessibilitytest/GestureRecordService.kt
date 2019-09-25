package com.coassets.android.accessibilitytest

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.WindowManager
import com.coassets.android.accessibilitytest.gesture.GestureCatchView
import com.coassets.android.accessibilitytest.gesture.GestureInfo
import com.coassets.android.accessibilitytest.gesture.GestureType
import kotlinx.android.synthetic.main.layout_record_floating_btn.view.*

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-22
 * UseDes:
 *
 */
@SuppressLint("InflateParams")
class GestureRecordService : Service() {

    private val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    private val gestureResultCallback = IGestureRecordBinder()

    private val gestureView: GestureCatchView by lazy {
        val gestureCatchView = GestureCatchView(this)
        gestureCatchView.setBackgroundColor(Color.WHITE)
        gestureCatchView.alpha = 0.5f
        gestureViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE

        gestureCatchView.onGestureListener = object : GestureCatchView.SimpleOnGestureListener() {
            override fun onGestureFinish(gestureType: GestureType, gestureInfo: GestureInfo) {
                //TODO 是否有可能不断 enable GestureCatchView
                //完成一个手势

                gestureInfo.delayTime = 500
                enableGestureCatchView(false)
                dispatchGesture(gestureInfo)
            }
        }
        gestureCatchView
    }

    private val gestureViewParams by lazy {
        val params = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        params.format = PixelFormat.RGBA_8888
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params
    }

    private val recordBtn by lazy {
        val recordBtn = LayoutInflater.from(this).inflate(
            R.layout.layout_record_floating_btn,
            null
        ) as MovableLayout
        val layRecord = recordBtn.layRecord
        val tvRecord = recordBtn.tvRecord
        val imRecord = recordBtn.imRecord
        val layClose = recordBtn.layClose;
        tvRecord.text = "开始"

        layRecord.setOnClickListener {
            it.isActivated = !it.isActivated
            if (it.isActivated) {
                //Start
                imRecord.setImageResource(R.drawable.ic_stop_white)
                tvRecord.base = SystemClock.elapsedRealtime()
                tvRecord.start()
                startRecord()


            } else {
                //STOP
                imRecord.setImageResource(R.drawable.ic_circle_white)
                tvRecord.stop()
                stopRecord()

            }
        }

        layClose.setOnClickListener {
            tvRecord.stop()
            windowManager.removeView(recordBtn)
            windowManager.removeView(gestureView)
            stopSelf()
        }
        recordBtn
    }


    inner class IGestureRecordBinder : Binder() {

        fun getService(): GestureRecordService {
            return this@GestureRecordService
        }

        fun setOnGestureRecordedListener(listener: OnGestureRecordedListener?) {
            onGestureRecordedListener = listener
        }

        fun onResult(isCancel: Boolean) {
            enableGestureCatchView(true)
        }

    }

    private var onGestureRecordedListener: OnGestureRecordedListener? = null

    interface OnGestureRecordedListener {
        fun onRecorded(gestureInfo: GestureInfo)
    }


    override fun onBind(intent: Intent?): IBinder? {
        windowManager.addView(gestureView, gestureViewParams)
        windowManager.addView(recordBtn, recordBtn.windowLayoutParams)
        return gestureResultCallback
    }


    private fun dispatchGesture(gestureInfo: GestureInfo) {
        onGestureRecordedListener?.onRecorded(gestureInfo)
        //GestureAccessibility.startGesture(this, gestureInfo)
    }

    private fun startRecord() {
        enableGestureCatchView(true)
        gestureView.startRecord()
    }

    private fun stopRecord() {
        enableGestureCatchView(false)
        gestureView.stopRecord()
    }


    private fun enableGestureCatchView(enable: Boolean) {
        if (enable) {
            gestureViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            gestureView.alpha = 0.5f
            //gestureViewParams.alpha = 1f
        } else {
            gestureViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            gestureView.alpha = 1f
           //gestureViewParams.alpha = 0.5f
        }

        //TODO 会有延迟
        windowManager.updateViewLayout(gestureView, gestureViewParams)
    }


}