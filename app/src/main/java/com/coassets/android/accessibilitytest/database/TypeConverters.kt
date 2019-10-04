package com.coassets.android.accessibilitytest.database

import androidx.room.TypeConverter
import com.coassets.android.accessibilitytest.ParcelableUtil
import com.coassets.android.accessibilitytest.gesture.GestureInfoBundle

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-10-02
 * UseDes:
 *
 */
class GestureInfoStoreConverter {

    @TypeConverter
    fun gestureToByteArray(gesture: GestureInfoBundle): ByteArray {
        return ParcelableUtil.marshall(gesture)
    }

    @TypeConverter
    fun byteArrayToGesture(byteArray: ByteArray): GestureInfoBundle {
        return ParcelableUtil.unmarshall(byteArray, GestureInfoBundle.CREATOR)
    }


}