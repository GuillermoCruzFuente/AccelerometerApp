package com.guillermo.accelerometer

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guillermo.accelerometer.classes.RealTimeChart
import kotlinx.android.synthetic.main.activity_charts.*

class ChartsActivity : AppCompatActivity(), SensorEventListener{
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var thread: Thread? = null
    private var plotData = true

    private lateinit var xAxisChart: RealTimeChart
    private lateinit var yAxisChart: RealTimeChart
    private lateinit var zAxisChart: RealTimeChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        if (mAccelerometer != null) {
            mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI)
        }

        xAxisChart = RealTimeChart(chartX, Color.RED)
        xAxisChart.applySettings()

        yAxisChart = RealTimeChart(chartY, Color.GREEN)
        yAxisChart.applySettings()

        zAxisChart = RealTimeChart(chartZ, Color.BLUE)
        zAxisChart.applySettings()

        feedMultiple()
    }

    private fun feedMultiple() {
        if (thread != null) {
            thread!!.interrupt()
        }
        thread = Thread {
            while (true) {
                plotData = true
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }

    override fun onPause() {
        super.onPause()
        if (thread != null) {
            thread!!.interrupt()
        }
        mSensorManager!!.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (plotData) {
            xAxisChart.addPoint(event.values[0])
            yAxisChart.addPoint(event.values[1])
            zAxisChart.addPoint(event.values[2])
            plotData = false
        }


    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onDestroy() {
        mSensorManager!!.unregisterListener(this@ChartsActivity)
        thread!!.interrupt()
        super.onDestroy()
    }
}