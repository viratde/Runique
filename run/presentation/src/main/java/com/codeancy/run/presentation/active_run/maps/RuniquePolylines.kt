package com.codeancy.run.presentation.active_run.maps

import android.icu.lang.UCharacter.JoiningType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.codeancy.core.domain.location.LocationTimeStamp
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline


@Composable
fun RuniquePolylines(
    modifier: Modifier = Modifier,
    locations: List<List<LocationTimeStamp>>
) {

    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { location1, location2 ->
                PolylineUi(
                    location1 = location1.location.location,
                    location2 = location2.location.location,
                    color = PolylineColorCalculator.locationsToColor(
                        location1 = location1,
                        location2 = location2
                    )
                )
            }
        }.flatten()
    }


    polylines.forEach { polyLine ->
        Polyline(
            points = listOf(
                LatLng(polyLine.location1.lat, polyLine.location1.long),
                LatLng(polyLine.location2.lat, polyLine.location2.long),
            ),
            color = polyLine.color,
            jointType = JointType.BEVEL
        )
    }

}