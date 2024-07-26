package com.codeancy.analytics.domain

import kotlin.time.Duration

data class AnalyticsValue(
    val totalDistanceRun: Int = 0,
    val totalTimeRun: Duration = Duration.ZERO,
    val fastestEverRun: Double = 0.0,
    val avgDistancePerRun: Double = 0.0,
    val avgPace: Double = 0.0
)
