package com.coassets.android.accessibilitytest.gesture

import android.gesture.Gesture
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.coassets.android.accessibilitytest.ParcelableUtil

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-10
 * UseDes:
 *
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Entity
@TypeConverters(GestureConverter::class)
data class GestureInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val gestureType: GestureType,
    val gesture: Gesture,
    var delayTime: Long,
    var duration: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        GestureType.valueOf(parcel.readString()),
        parcel.readParcelable(Gesture::class.java.classLoader),
        parcel.readLong(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(gestureType.name)
        parcel.writeParcelable(gesture, flags)
        parcel.writeLong(delayTime)
        parcel.writeLong(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "GestureInfo(id=$id, gestureType=$gestureType, delayTime=$delayTime, duration=$duration)"
    }

    companion object CREATOR : Parcelable.Creator<GestureInfo> {
        override fun createFromParcel(parcel: Parcel): GestureInfo {
            return GestureInfo(parcel)
        }

        override fun newArray(size: Int): Array<GestureInfo?> {
            return arrayOfNulls(size)
        }
    }


}


class GestureConverter {

    @TypeConverter
    fun gestureToByteArray(gesture: Gesture): ByteArray {
        return ParcelableUtil.marshall(gesture)
    }

    @TypeConverter
    fun byteArrayToGesture(byteArray: ByteArray): Gesture {
        return ParcelableUtil.unmarshall(byteArray, Gesture.CREATOR)
    }

    @TypeConverter
    fun gestureTypeToString(gestureType: GestureType): String {
        return gestureType.toString()
    }

    @TypeConverter
    fun stringToGestureType(gestureType: String): GestureType {
        return GestureType.valueOf(gestureType)
    }



}




