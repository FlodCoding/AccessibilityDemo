package com.coassets.android.accessibilitytest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.coassets.android.accessibilitytest.floating.FloatingService
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    lateinit var mediaProjection: MediaProjection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            //startFloatingService()
            //  screenshot()
            //screenshot()

            startActivity(Intent(this,TestActivity::class.java))
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun startFloatingService() {

        if (Settings.canDrawOverlays(this)) {
            startService(Intent(this, FloatingService::class.java))
        } else {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
            ActivityCompat.startActivityForResult(
                this,
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName)
                ), 0, null
            )


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
                startService(Intent(this, FloatingService::class.java))
            }
        } else if (requestCode == 1 && data != null) {
            Toast.makeText(this, "MediaProjectionManager", Toast.LENGTH_SHORT).show()
            val mediaManager =
                application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = mediaManager.getMediaProjection(resultCode, data)
            createVirtualDisplay()
        }

    }


    fun screenshot() {
//        val mediaManager =
//            application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
//        startActivityForResult(mediaManager.createScreenCaptureIntent(), 1)
        //startActivityForResult(mediaManager.createScreenCaptureIntent(), 1)

        val intent = Intent("com.android.camera.action.CROP")

    }

    fun createVirtualDisplay() {
        val metric = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(metric)

        val imageReader = ImageReader.newInstance(
            metric.widthPixels,
            metric.heightPixels,
            PixelFormat.RGBA_8888,
            1
        )


        mediaProjection.createVirtualDisplay(
            "Screenshot", metric.widthPixels, metric.heightPixels, metric.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.surface, object :
                VirtualDisplay.Callback() {
                override fun onResumed() {
                    Toast.makeText(applicationContext, "onResumed", Toast.LENGTH_SHORT).show()
                }

                override fun onStopped() {
                    Toast.makeText(applicationContext, "onStopped", Toast.LENGTH_SHORT).show()
                }

                override fun onPaused() {
                    Toast.makeText(applicationContext, "onPaused", Toast.LENGTH_SHORT).show()
                }
            }, null
        )



        SystemClock.sleep(6000)
        val image = imageReader.acquireNextImage()

        val width = image.width
        val height = image.height
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        val bitmap =
            Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)

        bitmap.copyPixelsFromBuffer(buffer)
        image.close()

        findViewById<ImageView>(R.id.image).setImageBitmap(bitmap)

    }
}
