package com.codeancy.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeancy.core.domain.location.Location
import com.codeancy.core.domain.run.Run
import com.codeancy.core.domain.run.RunRepository
import com.codeancy.core.domain.util.Result
import com.codeancy.core.presentation.ui.asUiText
import com.codeancy.run.domain.LocationDataCalculator
import com.codeancy.run.domain.RunningTracker
import com.codeancy.run.presentation.active_run.service.ActiveRunService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(
    private val runningTracker: RunningTracker,
    private val runRepository: RunRepository
) : ViewModel() {

    var state by mutableStateOf(
        ActiveRunState(
            shouldTrack = ActiveRunService.isServiceActive && runningTracker.isTracking.value,
            hasStartedRunning = ActiveRunService.isServiceActive
        )
    )
        private set

    private val eventChannel = Channel<ActiveRunEvent>()

    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow {
        state.shouldTrack
    }.stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val _hasLocationPermission = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack,
        _hasLocationPermission
    ) { shouldTrack, hasPermission ->
        shouldTrack && hasPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {

        _hasLocationPermission.onEach { hasPermission ->
            if (hasPermission) {
                runningTracker.startObservingLocation()
            } else {
                runningTracker.stopObservingLocation()
            }
        }.launchIn(viewModelScope)

        isTracking.onEach { isTracking ->
            runningTracker.setIsTracking(isTracking)
        }.launchIn(viewModelScope)

        runningTracker.currentLocation.onEach {
            state = state.copy(
                currentLocation = it?.location
            )
        }.launchIn(viewModelScope)

        runningTracker.runData.onEach { runData ->
            state = state.copy(runData = runData)
        }.launchIn(viewModelScope)

        runningTracker.elapsedTime.onEach { duration ->
            state = state.copy(
                elapsedTime = duration
            )
        }.launchIn(viewModelScope)


    }

    fun onAction(action: ActiveRunAction) {
        when (action) {

            is ActiveRunAction.OnRunProcessed -> {
                finishRun((action.mapPictureByte))
            }

            is ActiveRunAction.SubmitLocationPermissionInfo -> {

                _hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showLocationRationale = action.showLocationPermissionRationale
                )

            }

            is ActiveRunAction.SubmitNotificationPermissionInfo -> {
//                _hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showNotificationRationale = action.showNotificationPermissionRationale
                )

            }

            ActiveRunAction.OnFinishRunClick -> {
                state = state.copy(
                    isRunFinished = true,
                    isSavingRun = true
                )
            }

            ActiveRunAction.OnResumeRunClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }

            ActiveRunAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }

            ActiveRunAction.DismissRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }

            ActiveRunAction.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
            }
        }
    }

    private fun finishRun(mapPictureBytes: ByteArray) {

        val locations = state.runData.locations
        if (locations.isEmpty() || locations.first().size <= 1) {
            state = state.copy(
                isSavingRun = false
            )
            return
        }

        viewModelScope.launch {

            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUtc = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                distanceMeters = state.runData.distanceMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                maxSpeedKmh = LocationDataCalculator.getMaxSpeedKmh(state.runData.locations),
                totalElevationMts = LocationDataCalculator.getTotalElevationMeters(state.runData.locations),
                mapPictureUrl = null
            )

            runningTracker.finishRun()

            when (val result = runRepository.upsertRun(run, mapPictureBytes)) {
                is Result.Error -> {
                    eventChannel.send(
                        ActiveRunEvent.Error(result.error.asUiText())
                    )
                }

                is Result.Success -> {
                    eventChannel.send(
                        ActiveRunEvent.RunSaved
                    )
                }
            }
            state = state.copy(isSavingRun = false)
        }

    }

    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunService.isServiceActive) {
            runningTracker.stopObservingLocation()
        }
    }

}