package com.codeancy.run.network

import com.codeancy.core.domain.location.Location
import com.codeancy.core.domain.run.Run
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds


fun RunDto.toRun(): Run {

    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(lat, long),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMts = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )

}

fun Run.toRunDto(): RunDto {

    return RunDto(
        id = id!!,
        distanceMeters = distanceMeters,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        durationMillis = duration.inWholeMilliseconds,
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMts,
        mapPictureUrl = mapPictureUrl
    )

}

fun Run.toCreateRunRequest(): CreateRunRequest {

    return CreateRunRequest(
        id = id!!,
        distanceMeters = distanceMeters,
        durationMillis = duration.inWholeMilliseconds,
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMts,
        epochMillis = dateTimeUtc.toEpochSecond() * 1000L
    )

}