package com.coassets.android.accessibilitytest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach

class UnlockGestureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_gesture)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        toolBar.inflateMenu(R.menu.menu_unlock_gesture)
        toolBar.menu.forEach { it.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) }

        setActionBar(toolBar)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_unlock_gesture, menu)
        return true
    }

}
