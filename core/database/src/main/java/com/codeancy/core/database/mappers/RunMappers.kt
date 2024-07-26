package com.codeancy.core.database.mappers

import com.codeancy.core.database.model.RunEntity
import com.codeancy.core.domain.location.Location
import com.codeancy.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds


fun RunEntity.toRun(): Run {

    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
        location = Location(lat = latitude, long = longitude),
        maxSpeedKmh = maxSpeedKmh,
        distanceMeters = distanceMeters,
        mapPictureUrl = mapPictureUrl,
        totalElevationMts = totalElevationMeters
    )

}


fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        durationMillis = duration.inWholeMilliseconds,
        maxSpeedKmh = maxSpeedKmh,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        latitude = location.lat,
        longitude = location.long,
        distanceMeters = distanceMeters,
        totalElevationMeters = totalElevationMts,
        mapPictureUrl = mapPictureUrl,
        avgSpeedKmh = avgSpeedKmh
    )
}