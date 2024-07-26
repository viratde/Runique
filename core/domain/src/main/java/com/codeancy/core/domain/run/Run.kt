package com.codeancy.core.domain.run

import com.codeancy.core.domain.location.Location
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id: String?, // this will be null for new run,
    val duration: Duration,
    val dateTimeUtc: ZonedDateTime,
    val distanceMeters: Int,
    val location: Location,
    val maxSpeedKmh: Double,
    val totalElevationMts: Int,
    val mapPictureUrl: String?
) {

    val avgSpeedKmh
        get() = (distanceMeters / 1000.0) / duration.toDouble(DurationUnit.HOURS)

}
