package com.codeancy.run.domain

import com.codeancy.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObserver {

    fun observer(interval: Long): Flow<LocationWithAltitude>

}