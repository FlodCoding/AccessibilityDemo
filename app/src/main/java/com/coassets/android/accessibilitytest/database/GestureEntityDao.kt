package com.coassets.android.accessibilitytest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coassets.android.accessibilitytest.gesture.GestureEntity

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-18
 * UseDes:
 *
 */

@Dao
interface GestureEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: GestureEntity):Array<Long>

    @Query("SELECT * FROM gestureentity")
    suspend fun getAll(): List<GestureEntity>


}