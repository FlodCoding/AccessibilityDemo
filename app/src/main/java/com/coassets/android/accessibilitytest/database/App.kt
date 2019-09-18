package com.coassets.android.accessibilitytest.database

import android.app.Application
import com.flodcoding.task_j.data.database.AppDatabase

/**
 * SimpleDes:
 * Creator: Flood
 * Date: 2019-09-18
 * UseDes:
 *
 */
class App: Application(){
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}