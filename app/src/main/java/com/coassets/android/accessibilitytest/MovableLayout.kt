package com.coassets.android.accessibilitytest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.LinearLayout

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-22
 * UseDes:
 *
 */
@SuppressLint("InflateParams")
class MovableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    val windowLayoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            windowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        windowLayoutParams.format = PixelFormat.RGBA_8888
        windowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowLayoutParams.gravity = Gravity.START
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
    }


    private var tempX: Float = 0f
    private var tempY: Float = 0f
    private var isAfterMoved = false
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                tempX = event.rawX
                tempY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val nowX = event.rawX
                val nowY = event.rawY
                val movedX = nowX - tempX
                val movedY = nowY - tempY
                tempX = nowX
                tempY = nowY
                windowLayoutParams.x = (windowLayoutParams.x + movedX).toInt()
                windowLayoutParams.y = (windowLayoutParams.y + movedY).toInt()
                val windowManager =
                    context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.updateViewLayout(this, windowLayoutParams)
                isAfterMoved = true
            }
            MotionEvent.ACTION_UP -> {
                val isMoved = isAfterMoved
                isAfterMoved = false
                return isMoved
            }

        }
        return super.onInterceptTouchEvent(event)
    }

}