package com.coassets.android.accessibilitytest

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.view.Gravity
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
class GestureRecordService : Service() {

    private lateinit var gestureCatchView: GestureCatchView

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        gestureCatchView = initGestureView(windowManager)
        initRecordBtn(windowManager)
        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("InflateParams")
    private fun initRecordBtn(windowManager: WindowManager) {

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
        }


        windowManager.addView(recordBtn, recordBtn.windowLayoutParams)

    }

    private fun initGestureView(windowManager: WindowManager): GestureCatchView {

        val gestureCatchView = GestureCatchView(this)
        val params = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        params.format = PixelFormat.RGBA_8888
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.START
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL


        gestureCatchView.onGestureListener = object : GestureCatchView.SimpleOnGestureListener() {
            override fun onGestureFinish(gestureType: GestureType, gestureInfo: GestureInfo) {
                //TODO 是否有可能不断 enable GestureCatchView
                //完成一个手势
                dispatchGesture(gestureInfo)
            }
        }

        windowManager.addView(gestureCatchView, params)
        return gestureCatchView
    }


    private fun dispatchGesture(gestureInfo: GestureInfo) {

        GestureAccessibility.startGesture(this, gestureInfo)
    }

    private fun startRecord() {
        gestureCatchView.startRecord()
    }

    private fun stopRecord() {
        gestureCatchView.stopRecord()
    }


}