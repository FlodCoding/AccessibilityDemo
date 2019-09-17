package com.coassets.android.accessibilitytest.gesture

import android.gesture.Gesture

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-10
 * UseDes:
 *
 */
data class GestureInfo(

    val gestureType: GestureType,
    val gesture: Gesture,
    val delayTime: Long,
    val duration: Long
)




