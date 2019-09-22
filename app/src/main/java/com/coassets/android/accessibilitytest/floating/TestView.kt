package com.coassets.android.accessibilitytest.floating

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-21
 * UseDes:
 *
 */
class TestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }


    init {
    }

   /* override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }*/

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d("FloatingService",event.toString())
        return super.dispatchTouchEvent(event)
    }
}