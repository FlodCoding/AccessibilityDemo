package com.coassets.android.accessibilitytest.gesture

import android.os.Parcel
import android.os.Parcelable

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-10-02
 * UseDes:
 *
 */

class GestureInfoBundle(
    val gestureInfoList: List<GestureInfo>) : Parcelable {
    constructor(parcel: Parcel) : this(

        parcel.createTypedArrayList(GestureInfo.CREATOR)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(gestureInfoList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GestureInfoBundle> {
        override fun createFromParcel(parcel: Parcel): GestureInfoBundle {
            return GestureInfoBundle(parcel)
        }

        override fun newArray(size: Int): Array<GestureInfoBundle?> {
            return arrayOfNulls(size)
        }
    }
}

