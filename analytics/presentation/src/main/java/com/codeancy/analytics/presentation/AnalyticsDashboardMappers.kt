package com.codeancy.analytics.presentation

import com.codeancy.analytics.domain.AnalyticsValue
import com.codeancy.core.presentation.ui.formatted
import com.codeancy.core.presentation.ui.toFormattedKm
import com.codeancy.core.presentation.ui.toFormattedKmh
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


fun Duration.toFormattedTotalTime(): String {

    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60

    return "${days}d ${hours}h ${minutes}m"
}

fun AnalyticsValue.toAnalyticsDashboardState(): AnalyticsDashboardState {
    return AnalyticsDashboardState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        avgPace = avgPace.seconds.formatted(),
        avgDistancePerRun = (avgDistancePerRun / 1000.0).toFormattedKm()
    )
}