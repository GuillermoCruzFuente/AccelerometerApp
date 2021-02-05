package com.guillermo.accelerometer

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_charts.*

class ChartsActivity : AppCompatActivity(), SensorEventListener{
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null

    private var thread: Thread? = null
    private var plotData = true

//    private var chartX: LineChart? = null
//    private var chartY: LineChart? = null
//    private var chartZ: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        if (mAccelerometer != null) {
            mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)
        }

//        chartX = findViewById<View>(R.id.chartY) as LineChart

        // enable description text
        chartX!!.description.isEnabled = false

        // enable touch gestures
        chartX!!.setTouchEnabled(false)

        // enable scaling and dragging
        chartX!!.isDragEnabled = false
        chartX!!.setScaleEnabled(false)
        chartX!!.setDrawGridBackground(false)

        // if disabled, scaling can be done on x- and y-axis separately
        chartX!!.setPinchZoom(false)

        // set an alternative background color
        chartX!!.setBackgroundColor(Color.GRAY)
        val data = LineData()
        data.setValueTextColor(Color.LTGRAY)

        // add empty data
        chartX!!.data = data

        // get the legend (only possible after setting data)
        val l = chartX!!.legend
        l.isEnabled = false

        // modify the legend ...
//        l.form = Legend.LegendForm.LINE
//        l.textColor = Color.WHITE

        val xl = chartX!!.xAxis
//        xl.textColor = Color.WHITE
//        xl.setDrawGridLines(true)
//        xl.setAvoidFirstLastClipping(true)
//        xl.isEnabled = true
        xl.isEnabled = false

        val leftAxis = chartX!!.axisLeft
        leftAxis.isEnabled = true
        leftAxis.textColor = Color.LTGRAY
        leftAxis.setDrawGridLines(true)
        leftAxis.axisMaximum = 20f
        leftAxis.axisMinimum = -20f

        leftAxis.setDrawGridLines(false)
//        leftAxis.setDrawGridLines(true)
        val rightAxis = chartX!!.axisRight
        rightAxis.isEnabled = false
        chartX!!.axisLeft.setDrawGridLines(false)
        chartX!!.xAxis.setDrawGridLines(false)
        chartX!!.setDrawBorders(false)
        feedMultiple()
    }

    private fun addEntry(event: SensorEvent) {
        val data = chartX!!.data
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
//            data.addEntry(Entry(set.entryCount.toFloat(), event.values[0].absoluteValue), 0)
            data.addEntry(Entry(set.entryCount.toFloat(), event.values[1]), 0)
            data.notifyDataChanged()

            // let the chart know it's data has changed
            chartX!!.notifyDataSetChanged()

            // limit the number of visible entries
            chartX!!.setVisibleXRangeMaximum(500f)
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chartX!!.moveViewToX(data.entryCount.toFloat())
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 2f
        set.color = Color.BLACK
        set.isHighlightEnabled = false
        set.setDrawValues(false)
        set.setDrawCircles(false)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }

    private fun feedMultiple() {
        if (thread != null) {
            thread!!.interrupt()
        }
        thread = Thread {
            while (true) {
                plotData = true
                try {
                    Thread.sleep(2)
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
            addEntry(event)
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