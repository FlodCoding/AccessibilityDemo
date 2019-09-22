package com.coassets.android.accessibilitytest

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-22
 * UseDes:
 *
 */
class GestureRecordService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initFloatingBtn()
        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("InflateParams")
    private fun initFloatingBtn() {
        /*val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val recordFloatingBtn = LayoutInflater.from(applicationContext).inflate(R.layout.layout_record_floating_btn,null) as MovableButton

        val layStartRecord = recordFloatingBtn.layRecord
       // val tvRecord = recordFloatingBtn.tvRecord
        val layClose = recordFloatingBtn.layClose
        recordFloatingBtn.layRecord.setOnClickListener {
           // tvRecord.start()
        }

        recordFloatingBtn.layClose.setOnClickListener {
            windowManager.removeView(recordFloatingBtn)
        }

        val layoutParams = WindowManager.LayoutParams()
        recordFloatingBtn.layoutParams = layoutParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.START
        layoutParams.x = 100
        layoutParams.y = 100
        layoutParams.flags =   WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

        //recordFloatingBtn.setOnTouchListener(RecordFloatingBtnOnTouchListener(layoutParams))

        //recordFloatingBtn.setOnKeyListener()

        windowManager.addView(recordFloatingBtn, layoutParams)*/
        val windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val movableButton = MovableButton(this)
        windowManager.addView(movableButton, movableButton.windowLayoutParams)


    }


    //TODO 自动停靠
    private class RecordFloatingBtnOnTouchListener(private val layoutParams: WindowManager.LayoutParams) :
        View.OnTouchListener {
        private var x: Float = 0f
        private var y: Float = 0f

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX
                    y = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX
                    val nowY = event.rawY
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.x = (layoutParams.x + movedX).toInt()
                    layoutParams.y = (layoutParams.y + movedY).toInt()
                    val windowManager =
                        view?.context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    windowManager.updateViewLayout(view, layoutParams)
                }


            }
            return false
        }
    }
}