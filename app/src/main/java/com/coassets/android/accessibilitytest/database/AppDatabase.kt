package com.flodcoding.task_j.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coassets.android.accessibilitytest.database.GestureDao
import com.coassets.android.accessibilitytest.gesture.GestureInfo

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-08-12
 * UseDes:
 *
 */
@Database(entities = [GestureInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gestureDao(): GestureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        fun getInstance(): AppDatabase = INSTANCE!!


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "AppDatabase.db"
            ).build()
    }


}