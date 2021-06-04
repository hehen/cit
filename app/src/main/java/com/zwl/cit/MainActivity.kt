package com.zwl.cit

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.samples.cameracalibration.CameraCalibrationActivity
import org.opencv.samples.colorblobdetect.ColorBlobDetectionActivity
import org.opencv.samples.facedetect.FdActivity
import org.opencv.samples.imagemanipulations.ImageManipulationsActivity
import org.opencv.samples.puzzle15.Puzzle15Activity
import org.opencv.samples.tutorial1.Tutorial1Activity
import org.opencv.samples.tutorial2.Tutorial2Activity
import org.opencv.samples.tutorial3.Tutorial3Activity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var car: Car

    @FootLight
    @Inject
    lateinit var footLight: Light

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //for 6.0 and 6.0 above, apply permission
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
        // Example of a call to a native method
        findViewById<Button>(R.id.btn1).text = stringFromJNI()
        findViewById<Button>(R.id.btn2).setOnClickListener {
            startActivity(Intent(this@MainActivity, Puzzle15Activity::class.java))
        }
        findViewById<Button>(R.id.btn3).setOnClickListener {
            startActivity(Intent(this@MainActivity, CameraCalibrationActivity::class.java))
        }
        findViewById<Button>(R.id.btn4).setOnClickListener {
            startActivity(Intent(this@MainActivity, ColorBlobDetectionActivity::class.java))
        }

        findViewById<Button>(R.id.btn5).setOnClickListener {
            startActivity(Intent(this@MainActivity, FdActivity::class.java))
        }
        findViewById<Button>(R.id.btn6).setOnClickListener {
            startActivity(Intent(this@MainActivity, ImageManipulationsActivity::class.java))
        }
        findViewById<Button>(R.id.btn7).setOnClickListener {
            startActivity(Intent(this@MainActivity, Tutorial1Activity::class.java))
        }
        findViewById<Button>(R.id.btn8).setOnClickListener {
            startActivity(Intent(this@MainActivity, Tutorial2Activity::class.java))
        }

        findViewById<Button>(R.id.btn9).setOnClickListener {
            startActivity(Intent(this@MainActivity, Tutorial3Activity::class.java))
        }

        car.use()
        footLight.use()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}