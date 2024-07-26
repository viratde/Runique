package com.codeancy.run.presentation.run_overview.mappers

import com.codeancy.core.domain.run.Run
import com.codeancy.core.presentation.ui.formatted
import com.codeancy.core.presentation.ui.toFormattedKm
import com.codeancy.core.presentation.ui.toFormattedKmh
import com.codeancy.core.presentation.ui.toFormattedMeters
import com.codeancy.core.presentation.ui.toFormattedPace
import com.codeancy.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun Run.toRunUi(): RunUi {

    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())

    val formattedDateTime =
        DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma").format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMts.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )

}