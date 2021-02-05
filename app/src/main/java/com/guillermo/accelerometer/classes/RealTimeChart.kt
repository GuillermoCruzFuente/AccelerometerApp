package com.guillermo.accelerometer.classes

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet

class RealTimeChart(
    private val chart: LineChart,
    private val colorDataSet: Int = Color.BLACK
) {

    fun applySettings() {
        chart.description.isEnabled = false

    }

    private fun createDataSet(): LineDataSet {
        val set = LineDataSet(null, "")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 2f
        set.color = colorDataSet
        set.isHighlightEnabled = false
        set.setDrawValues(false)
        set.setDrawCircles(false)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }
}