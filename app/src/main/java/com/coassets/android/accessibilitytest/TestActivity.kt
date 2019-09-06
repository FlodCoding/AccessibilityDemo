package com.coassets.android.accessibilitytest

import android.annotation.SuppressLint
import android.content.Intent
import android.gesture.Gesture
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    val list: ArrayList<Gesture> = ArrayList()
    lateinit var  gesture: Gesture


    lateinit var gestureDetector: GestureDetector

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        val gestureView = findViewById<GestureOverlayView>(R.id.layGesture)
        gestureView.addOnGesturingListener(object : GestureOverlayView.OnGesturingListener {
            override fun onGesturingStarted(p0: GestureOverlayView?) {
                Log.d("gestureView", "onGesturingStarted")
            }

            override fun onGesturingEnded(p0: GestureOverlayView?) {
                Log.d("gestureView", "onGesturingEnded")
                p0?.currentStroke
            }
        })

        gestureView.addOnGesturePerformedListener { gestureOverlayView, gesture ->
            Log.d("gestureView", "OnGesturePerformedListener：" + gesture.toPath())
            this.gesture  = gesture

        }

        gestureView.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return gestureDetector.onTouchEvent(p1)
            }

        })
        gestureDetector = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onShowPress(p0: MotionEvent?) {
                Log.d("gestureView", "onShowPress")
            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                Log.d("gestureView", "onSingleTapUp")
                return false
            }

            override fun onDown(p0: MotionEvent?): Boolean {
                Log.d("gestureView", "onDown")
                return false
            }

            override fun onFling(
                p0: MotionEvent?,
                p1: MotionEvent?,
                p2: Float,
                p3: Float
            ): Boolean {
                Log.d("gestureView", "onFling")
                return false
            }

            override fun onScroll(
                p0: MotionEvent?,
                p1: MotionEvent?,
                p2: Float,
                p3: Float
            ): Boolean {
                Log.d("gestureView", "onScroll")
                return false
            }

            override fun onLongPress(p0: MotionEvent?) {
                Log.d("gestureView", "onLongPress")
            }

        })





        findViewById<Button>(R.id.btClear).setOnClickListener {
            gestureView.clear(false)
        }


        findViewById<Button>(R.id.btReDraw).setOnClickListener {
            val intent = Intent(this, MyAccessibility::class.java)
            intent.putExtra("gesture", gesture)
            startService(intent)
        }
    }


}
