package com.coassets.android.accessibilitytest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coassets.android.accessibilitytest.gesture.GestureInfo

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-18
 * UseDes:
 *
 */

@Dao
interface GestureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg gestureInfo: GestureInfo):Array<Long>

    @Query("SELECT * FROM gestureinfo")
    suspend fun getAll(): List<GestureInfo>


}