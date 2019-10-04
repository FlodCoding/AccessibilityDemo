package com.coassets.android.accessibilitytest.gesture

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.coassets.android.accessibilitytest.ParcelableUtil

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-10-02
 * UseDes:
 *
 */
@Entity
@TypeConverters(GestureInfoStoreConverter::class)
data class GestureEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val gestureInfoBundle: GestureInfoBundle
)


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
