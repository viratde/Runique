package com.codeancy.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.codeancy.core.domain.location.LocationTimeStamp
import kotlin.math.abs


object PolylineColorCalculator {


    fun locationsToColor(
        location1: LocationTimeStamp,
        location2: LocationTimeStamp
    ): Color {

        val distance = location1.location.location.distanceTo(location2.location.location)
        val timeDiff =
            abs((location1.durationTimestamp - location2.durationTimestamp).inWholeSeconds)

        val speedKmh = (distance / timeDiff) * 3.6

        return interpolateColor(
            speedKmh = speedKmh,
            maxSpeed = 20.0,
            minSpeed = 5.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )
    }

    private fun interpolateColor(
        speedKmh: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color,
    ): Color {

        val ratio = ((speedKmh - minSpeed) / (maxSpeed - minSpeed)).coerceIn(0.0..1.0)

        val colorInt = if (ratio <= 0.5) {
            val midRatio = ratio / 0.5
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }

        return Color(color = colorInt)
    }


}