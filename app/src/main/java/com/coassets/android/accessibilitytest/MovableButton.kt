package com.coassets.android.accessibilitytest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.layout_record_floating_btn.view.*

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-22
 * UseDes:
 *
 */
@SuppressLint("InflateParams")
class MovableButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){

     val windowLayoutParams get():WindowManager.LayoutParams = WindowManager.LayoutParams()

     init {

        val root = LayoutInflater.from(context).inflate(R.layout.layout_record_floating_btn,null)

        val layStartRecord = root.layRecord
        // val tvRecord = recordFloatingBtn.tvRecord
        val layClose = root.layClose
       /* recordFloatingBtn.layRecord.setOnClickListener {
            // tvRecord.start()
        }

        recordFloatingBtn.layClose.setOnClickListener {
            windowManager.removeView(recordFloatingBtn)
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            windowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        windowLayoutParams.format = PixelFormat.RGBA_8888
        windowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowLayoutParams.gravity = Gravity.START
        windowLayoutParams.flags =   WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

         //addView(root)
        //windowManager.addView(recordFloatingBtn, windowLayoutParams)
    }


    private var tempX: Float = 0f
    private var tempY: Float = 0f
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
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
            }


        }
        return super.dispatchTouchEvent(event)
    }

}