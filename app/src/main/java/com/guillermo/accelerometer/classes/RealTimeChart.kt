package com.guillermo.accelerometer.classes

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class RealTimeChart(
        private val chart: LineChart,
        private val colorDataSet: Int = Color.BLACK
) {

    fun applySettings() {
        chart.data = LineData()
        chart.apply {

            description.isEnabled = false
            setTouchEnabled(false)
            setDrawGridBackground(false)
            setPinchZoom(false)
            setBackgroundColor(Color.GRAY)
            legend.isEnabled = false
            xAxis.isEnabled = false

            axisLeft.apply {
                isEnabled = true
                textColor = Color.LTGRAY
                setDrawGridLines(true)
                axisMaximum = 20f
                axisMinimum = -20f
            }

            axisRight.apply {
                isEnabled = false
                setDrawGridLines(true)
            }

            setDrawBorders(false)
        }
    }

    private fun createDataSet(): LineDataSet {
        val set = LineDataSet(null, "")
        return set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            lineWidth = 2f
            color = colorDataSet
            isHighlightEnabled = false
            setDrawValues(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.3f
        }
    }

    fun addPoint(point: Float) {
        val currentData = chart.data

        if (currentData != null) {
            var dataSet = currentData.getDataSetByIndex(0)

            if (dataSet == null) {
                dataSet = createDataSet()
                currentData.addDataSet(dataSet)
            }

            val xValueForCurrentPoint = dataSet.entryCount.toFloat()
            val newEntry = Entry(xValueForCurrentPoint, point)
            currentData.addEntry(newEntry, 0)
            currentData.notifyDataChanged()

            chart.notifyDataSetChanged()
            chart.setVisibleXRangeMaximum(100f)
            chart.moveViewToX(xValueForCurrentPoint)
        }
    }

}