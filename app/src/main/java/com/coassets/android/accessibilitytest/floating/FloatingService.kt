package com.coassets.android.accessibilitytest.floating

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button


/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-08-27
 * UseDes:
 *
 */
class FloatingService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showFloating()
        return super.onStartCommand(intent, flags, startId)
    }


    private fun showFloating() {
        if (Settings.canDrawOverlays(this)) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            // 新建悬浮窗控件
            val button = Button(applicationContext)
            button.text = "Floating Window"
            button.setBackgroundColor(Color.BLUE)
            //button.setOnTouchListener(FloatingOnTouchListener())

            // 设置LayoutParam
            val layoutParams = WindowManager.LayoutParams()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.width = 100;
            layoutParams.height = 100;
            layoutParams.x = 300;
            layoutParams.y = 300;

            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(button, layoutParams);

        }

    }

    private class FloatingOnTouchListener : View.OnTouchListener {
        private var x: Float = 0f
        private var y: Float = 0f

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
                    x = nowX;
                    y = nowY;
                    val layoutParams = WindowManager.LayoutParams()
                    layoutParams.x = (layoutParams.x + movedX).toInt()
                    layoutParams.y = (layoutParams.y + movedY).toInt()
                    val windowManager =
                        view?.context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    windowManager.updateViewLayout(view, layoutParams);
                }

            }
            return false;
        }
    }

}