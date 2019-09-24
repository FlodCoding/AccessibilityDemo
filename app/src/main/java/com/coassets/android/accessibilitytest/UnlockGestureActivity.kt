package com.coassets.android.accessibilitytest

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.coassets.android.accessibilitytest.gesture.GestureInfo
import com.flodcoding.task_j.data.database.AppDatabase
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_unlock_gesture.*
import kotlinx.coroutines.runBlocking


class UnlockGestureActivity : AppCompatActivity() {

    companion object {
        const val RQ_SELECT_IMAGE = 0x01
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentBar().transparentNavigationBar().init()

        setContentView(R.layout.activity_unlock_gesture)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //layGesture.startRecord()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_unlock_gesture, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.menu_reset_gesture -> {
                layGesture.startRecord()
                true
            }

            R.id.menu_reset_bg -> {
                layGesture.clear()
                /* startActivityForResult(
                     Intent(
                         Intent.ACTION_PICK,
                         MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                     ), RQ_SELECT_IMAGE
                 )*/
                runBlocking {
                    val result = AppDatabase.getInstance().gestureDao().getAll()
                    startAccessibility(ArrayList(result))
                }

                true
            }
            R.id.menu_done -> {
                val result = layGesture.stopRecord()

                runBlocking {
                    AppDatabase.getInstance().gestureDao().insert(result[0])
                }

                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            val imagePathUri = data?.data
            setBackground(imagePathUri)
        }
    }


    private fun setBackground(imagePathUri: Uri?) {
        imagePathUri ?: return

        Glide.with(this).load(imagePathUri)
            .into(image)

    }


    fun startAccessibility(list: ArrayList<GestureInfo>) {


        val intent = Intent(this, GestureAccessibility::class.java)
        intent.putExtra("gesture", list)
        startService(intent)
    }

}
