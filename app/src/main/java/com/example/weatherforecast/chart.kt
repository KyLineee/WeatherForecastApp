package com.example.weatherforecast

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import org.w3c.dom.Text
import java.lang.Float.min
import kotlin.random.Random

const val steps = 6
@Composable
fun chart(){
    val pointsList = getPointsList()
    val max = getMax(pointsList)
    val min = getMin(pointsList)
    val xAxisData = AxisData.Builder()
        .axisStepSize(50.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsList.size - 1)
        .labelData { i -> i.toString() + 'h'}
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(15.dp)
        .labelData {  i ->
            val yScale = (max - min) / steps.toFloat()
            String.format("%.1f", ((i * yScale) + min))
        }
        .build()
    val textMeasurer = rememberTextMeasurer()
    val style = TextStyle(
        fontSize = 20.sp,
        color = Color.Black,
    )
    val windowSize = LocalConfiguration.current.screenWidthDp
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsList,
                    LineStyle(color = Color.Green, width = 5.0f),
                    IntersectionPoint(
                        radius = 4.dp,
                        draw = { offset ->
                            val xPosition = min(offset.x, windowSize.toFloat() + 660f)
                            drawCircle(color = Color.Black, radius = 14f, center = offset)
                            drawText(
                                textMeasurer = textMeasurer,
                                text = "11",
                                style = style,
                                topLeft = Offset(
                                    x = xPosition - 30f,
                                    y = offset.y - 90f,
                                )
                            )
                        },
                    ),
                    SelectionHighlightPoint(color = Color.Black, alpha = 0.4f, radius = 5.dp),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.Transparent),
        backgroundColor = Color.Transparent
    )
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        lineChartData = lineChartData
    )
}
fun getPointsList(): List<Point> {
    val list = ArrayList<Point>()
    for (i in 0..23){
        list.add(
            Point(
                i.toFloat(),
                Random.nextInt(-30,30).toFloat()
            )
        )
    }
    return list
}

fun getMax(list: List<Point>): Float{
    var max = 0f
    list.forEach{point ->
        if(max < point.y) max = point.y
    }
    return max
}

fun getMin(list: List<Point>): Float{
    var min = 1000f
    list.forEach{point ->
        if(min > point.y) min = point.y
    }
    return min
}
