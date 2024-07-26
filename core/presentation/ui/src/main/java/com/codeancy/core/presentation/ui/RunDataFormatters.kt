package com.codeancy.core.presentation.ui

import java.util.Locale
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration


fun Duration.formatted(): String {

    val totalSeconds = inWholeSeconds

    val hours = String.format(Locale.getDefault(), "%02d", totalSeconds / 3600)
    val minutes = String.format(Locale.getDefault(), "%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format(Locale.getDefault(), "%02d", totalSeconds % 60)

    return "$hours:$minutes:$seconds"
}


fun Double.toFormattedKmh(): String {
    return "${this.roundToDecimalPlace(1)} Km/h"
}

fun Int.toFormattedMeters(): String {
    return "$this m"
}

fun Double.toFormattedKm(): String {
    return "${this.roundToDecimalPlace(1)}Km"
}

fun Duration.toFormattedPace(distanceKm: Double): String {
    if (this == Duration.ZERO || distanceKm <= 0.0) {
        return "-"
    }
    val secondsPerKm = (inWholeSeconds / distanceKm).roundToInt()
    val avgPaceMinutes = secondsPerKm / 60
    val avgPaceSeconds = String.format(Locale.getDefault(), "%02d", secondsPerKm % 60)
    return "$avgPaceMinutes:$avgPaceSeconds / Km"
}

private fun Double.roundToDecimalPlace(decimalCount: Int): Double {

    val factor = 10f.pow(decimalCount)

    return round(this * factor) / factor
}