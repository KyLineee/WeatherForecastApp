package com.example.weatherforecast


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.Point
import com.example.weatherforecast.data.WeatherModel
import java.lang.Math.abs
import kotlin.random.Random

@Composable
fun chart(list: List<WeatherModel>) {
    var listOffset = getPointsList(list)
    drawPoints(listOffset)
    drawText(listOffset)
}

@Composable
fun drawText(pointsList: List<Offset>) {
    val textMeasurer = rememberTextMeasurer()
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        itemsIndexed(
            pointsList
        ) { i, item ->
            Canvas(
                modifier = Modifier
                    .width(36.dp)
                    .height(200.dp)
            ) {
                var y = item.y - 200f
                if(y < 0) {
                    y += 2 * abs(y)
                }
                else {
                    y -= 2 * abs(y)
                }
                var yText = y.toString()
                drawText(
                    textMeasurer = textMeasurer,
                    text = yText,
                    topLeft = Offset(0f, item.y - 60f)
                )
            }
        }
    }
}

@Composable
fun drawPoints(pointsList: List<Offset>) {
    Canvas(modifier = Modifier.size(200.dp)) {
        drawPoints(
            points = pointsList,
            strokeWidth = 12f,
            pointMode = PointMode.Points,
            color = Color.Blue,
            cap = StrokeCap.Round
        )
        drawPoints(
            points = pointsList,
            strokeWidth = 4f,
            pointMode = PointMode.Polygon,
            color = Color.Blue,
            cap = StrokeCap.Round
        )
    }
}

fun getPointsList(list: List<WeatherModel>): List<Offset> {
    val listOffset = ArrayList<Offset>()
    var x = 0f
    var y = 0f
    for (i in 0 until list.size) {
        y = list[i].currentTemp.toFloat()
        if(y < 0) {
            y += 2 * abs(y)
        }
        else {
            y -= 2 * y
        }
        y += 200f
        listOffset.add(Offset(x, y))
        x += 100
    }
    return listOffset
}

fun getPointsListExample(): List<Offset> {
    val listOffset = ArrayList<Offset>()
    var x = 50f
    var y = 0f
    for (i in 0..10) {
        y = Random.nextInt(-30, 30).toFloat()
        if(y < 0) {
            y += 2 * abs(y)
        }
        else {
            y -= 2 * y
        }
        y += 200f
        listOffset.add(Offset(x, y))
        x += 100
    }
    return listOffset
}

fun getMax(list: List<Point>): Float {
    var max = 0f
    list.forEach { point ->
        if (max < point.y) max = point.y
    }
    return max
}

fun getMin(list: List<Point>): Float {
    var min = 1000f
    list.forEach { point ->
        if (min > point.y) min = point.y
    }
    return min
}
