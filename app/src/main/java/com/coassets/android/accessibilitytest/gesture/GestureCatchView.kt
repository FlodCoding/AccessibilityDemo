package com.coassets.android.accessibilitytest.gesture

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.gesture.Gesture
import android.gesture.GesturePoint
import android.gesture.GestureStroke
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.core.graphics.alpha
import com.coassets.android.accessibilitytest.R


/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-09
 * UseDes:
 * 1、点击点 √
 * 2、移动线  √
 * 3、笔画渐变 √
 * 4、资源释放
 * 5、多指处理
 * 6、载入手势或路径
 */
class GestureCatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEBUG = true
        private const val TAG = "GestureCatchView"

        const val DURATION: Int = 0
        const val NEXT: Int = 1
        const val KEEP: Int = 2
    }

    //kotlin not work
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DURATION, NEXT, KEEP)
    annotation class PathKeepStyle

    //attrs
    private var globalPoint: Boolean         //true:以屏幕为坐标系   false:以View为坐标系
    private var longPressDuration: Long      //长按触发时间
    private var pathTopLayer: Boolean

    private var minPath: Int                //path小于这个长度为点击事件
    private var pathWidth: Int
    private var pathColor: Int

    @PathKeepStyle
    private var pathKeepStyle: Int
    private var pathDuration: Long

    private var fadeEnabled: Boolean
    private var fadeDuration: Long


    private var isRecoding = false
    private var startTimeTemp: Long = 0   //开始手势的时间点（用来得到开始时间到手指触摸的时间）

    private var mGestureItemTemp: GestureItem? = null
    private val mGestureItemTempList: ArrayList<GestureItem> = ArrayList()
    private val mGestureInfoList: ArrayList<GestureInfo> = ArrayList()

    var onGestureListener: OnGestureListener? = null


    init {
        setWillNotDraw(false)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.GestureCatchView)
        globalPoint = typeArray.getBoolean(R.styleable.GestureCatchView_globalPoint, true)
        longPressDuration = typeArray.getInteger(R.styleable.GestureCatchView_longPressDuration, 1500).toLong()
        minPath = typeArray.getDimensionPixelSize(R.styleable.GestureCatchView_minPath, 12)
        pathWidth = typeArray.getDimensionPixelSize(R.styleable.GestureCatchView_pathWidth, 15)
        pathColor = typeArray.getColor(R.styleable.GestureCatchView_pathColor, Color.BLACK)
        pathKeepStyle = typeArray.getInteger(R.styleable.GestureCatchView_pathKeepStyle, DURATION)
        pathDuration = typeArray.getInteger(R.styleable.GestureCatchView_pathDuration, 0).toLong()
        pathTopLayer = typeArray.getInteger(R.styleable.GestureCatchView_pathLayer, 0) == 0

        fadeEnabled = typeArray.getBoolean(R.styleable.GestureCatchView_fadeEnabled, true)
        fadeDuration = typeArray.getInteger(R.styleable.GestureCatchView_fadeDuration, 1500).toLong()

        typeArray.recycle()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (pathTopLayer) {
            super.dispatchDraw(canvas)
            dispatchGestureItemDraw(canvas)
        } else {
            dispatchGestureItemDraw(canvas)
            super.dispatchDraw(canvas)
        }
    }


    private fun dispatchGestureItemDraw(canvas: Canvas) {
        for (item in mGestureItemTempList) {
            item.draw(canvas)
        }
    }


    override fun onDetachedFromWindow() {
        cancelAnimation()
        super.onDetachedFromWindow()

    }

    private fun cancelAnimation() {
        for (item in mGestureItemTempList) {
            item.cancelAnim()
        }
        mGestureItemTemp?.cancelAnim()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        processEvent(event)
        return true
    }


    private fun processEvent(event: MotionEvent) {
        val itemTemp = mGestureItemTemp

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // actionDown(event)
                if (pathKeepStyle == NEXT) {
                    //其他的Path开始消失
                    for (item in mGestureItemTempList) {
                        item.fadePath()
                    }
                }

                val gestureItem = GestureItem()
                gestureItem.startPath(event)
                mGestureItemTemp = gestureItem
                mGestureItemTempList.add(gestureItem)

            }

            MotionEvent.ACTION_MOVE -> {
                // actionMove(event)
                itemTemp?.addToPath(event)

            }

            MotionEvent.ACTION_UP -> {
                // actionUp()
                itemTemp?.endPath()
            }
            MotionEvent.ACTION_CANCEL -> {
                //actionUp(event)
            }

        }

        invalidate()

    }


    fun startRecord() {
        // 清理旧的手势
        clear()

        isRecoding = true
        startTimeTemp = System.currentTimeMillis()
    }


    fun stopRecord(): ArrayList<GestureInfo> {
        isRecoding = false
        return mGestureInfoList
    }

    fun clear() {
        cancelAnimation()
        mGestureItemTempList.clear()
        mGestureInfoList.clear()
        invalidate()
    }

    /* fun loadGesture(){

     }*/


    interface OnGestureListener {

        fun onGesturing(gesturePoint: GesturePoint)

        fun onGestureFinish(
            gestureType: GestureType,
            gestureInfo: GestureInfo
        )

        //fun onGestureCancel()
    }


    inner class GestureItem {
        private val mAnimator: ValueAnimator

        private val mPath: Path = Path()
        private val mPaint = Paint()
        private var mLastX: Float = 0f
        private var mLastY: Float = 0f
        private val mPointBuffer: ArrayList<GesturePoint> = ArrayList()

        private val mDelayTime: Long = if (startTimeTemp == 0L) 0 else System.currentTimeMillis() - startTimeTemp
        private var mGesture: Gesture? = null
        private var isActionDown = false
        private var isLongPress = false
        private var drawRequest = true

        private val recordMark: Boolean         //被标记为记录的Item


        private val longPressRunnable: Runnable = Runnable {
            val gesture = mGesture
            if (gesture != null) {
                gesture.addStroke(GestureStroke(mPointBuffer))
                onGestureDone(GestureType.LONG_PRESS, gesture)
                isLongPress = true
            }
            mPath
        }

        init {
            //init
            mPaint.strokeWidth = pathWidth.toFloat()
            mPaint.color = pathColor
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeJoin = Paint.Join.ROUND  //拐角圆形
            mPaint.strokeCap = Paint.Cap.ROUND    //开始和结尾加入圆形
            mPaint.isAntiAlias = true

            //initAnimator
            mAnimator = ValueAnimator.ofFloat(1f, 0f)
            mAnimator.interpolator = AccelerateInterpolator()
            mAnimator.duration = fadeDuration
            mAnimator.addUpdateListener {
                mPaint.alpha = (pathColor.alpha * it.animatedValue as Float).toInt()
                mPaint.strokeWidth = pathWidth * it.animatedValue as Float
                invalidate()
            }
            mAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    drawRequest = false
                }
            })


            recordMark = isRecoding
        }


        fun startPath(event: MotionEvent) {
            val x = event.x
            val y = event.y

            mPath.rewind()
            mPath.moveTo(x, y)
            mPath.lineTo(x, y)

            val pointX = if (globalPoint) event.rawX else x
            val pointY = if (globalPoint) event.rawY else y
            mPointBuffer.add(GesturePoint(pointX, pointY, event.eventTime))

            mLastX = x
            mLastY = y

            val gesture = mGesture
            if (gesture == null) {
                //从startRecord开始，第一次记录手势的时间点
                mGesture = Gesture()

                //开始计时，如果按下后不动，一定时间后触发长按
                isActionDown = true
                postDelayed(longPressRunnable, longPressDuration)
            }
        }

        fun addToPath(event: MotionEvent) {
            removeLongPressCallback()

            val x = event.x
            val y = event.y

            //取上一个点为控制点
            val controlX = mLastX
            val controlY = mLastY
            //上一个点与当前点的中点为结束点
            val endX = (controlX + x) / 2
            val endY = (controlY + y) / 2
            //构成光滑曲线
            mPath.quadTo(mLastX, mLastY, endX, endY)

            val pointX = if (globalPoint) event.rawX else x
            val pointY = if (globalPoint) event.rawY else y

            val gesturePoint = GesturePoint(pointX, pointY, event.eventTime)
            mPointBuffer.add(gesturePoint)

            mLastX = x
            mLastY = y

            onGestureListener?.onGesturing(gesturePoint)
        }

        fun endPath() {
            //如果手指抬起时小于最小的手势范围认为是Tab

            removeLongPressCallback()

            val gesture = mGesture
            if (gesture != null && !isLongPress) {
                gesture.addStroke(GestureStroke(mPointBuffer))
                if (gesture.length < minPath) {
                    onGestureDone(GestureType.TAP, gesture)
                } else {
                    onGestureDone(GestureType.GESTURE, gesture)
                }

            }

            isLongPress = false

        }


        private fun onGestureDone(gestureType: GestureType, gesture: Gesture) {
            //开始消失动画
            if (fadeEnabled && pathKeepStyle == DURATION) {
                mAnimator.startDelay = pathDuration
                mAnimator.start()
            }


            val curTime = System.currentTimeMillis()

            val gestureInfo = GestureInfo(
                gestureType = gestureType,
                gesture = gesture,
                delayTime = mDelayTime,
                duration = System.currentTimeMillis() - mDelayTime
            )

            if (recordMark)
                mGestureInfoList.add(gestureInfo)

            onGestureListener?.onGestureFinish(gestureType, gestureInfo)


            startTimeTemp = curTime
            mPointBuffer.clear()
            mGesture = null

        }


        fun fadePath() {
            if (fadeEnabled && !mAnimator.isStarted && drawRequest) {
                mAnimator.startDelay = pathDuration
                mAnimator.start()
            }
        }

        private fun removeLongPressCallback() {
            if (isActionDown) {
                //移除长按触发
                isActionDown = false
                handler.removeCallbacks(longPressRunnable)
            }
        }


        fun draw(canvas: Canvas) {
            if (drawRequest)
                canvas.drawPath(mPath, mPaint)
        }

        fun cancelAnim() {
            mAnimator.cancel()
        }


    }


    private fun log(msg: String) {
        if (DEBUG)
            Log.d(TAG, msg)
    }


}


