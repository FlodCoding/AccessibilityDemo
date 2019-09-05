package com.coassets.android.accessibilitytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        /*val display = windowManager.defaultDisplay

        val layPara = window.attributes
        layPara.width = (display.width * 0.6).toInt()
        layPara.height = (display.height * 0.6).toInt()


        window.attributes = layPara*/

    }
}
