package com.codeancy.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeancy.core.presentation.design_system.RuniqueTheme
import com.codeancy.core.presentation.design_system.StartIcon
import com.codeancy.core.presentation.design_system.StopIcon
import com.codeancy.core.presentation.design_system.components.RuniqueActionButton
import com.codeancy.core.presentation.design_system.components.RuniqueDialog
import com.codeancy.core.presentation.design_system.components.RuniqueFloatingActionButton
import com.codeancy.core.presentation.design_system.components.RuniqueOutlinedActionButton
import com.codeancy.core.presentation.design_system.components.RuniqueScaffold
import com.codeancy.core.presentation.design_system.components.RuniqueToolbar
import com.codeancy.core.presentation.ui.ObserverAsEvents
import com.codeancy.run.presentation.R
import com.codeancy.run.presentation.active_run.components.RunDataCard
import com.codeancy.run.presentation.active_run.maps.TrackerMap
import com.codeancy.run.presentation.active_run.service.ActiveRunService
import com.codeancy.run.presentation.util.hasLocationPermission
import com.codeancy.run.presentation.util.hasNotificationPermission
import com.codeancy.run.presentation.util.shouldShowLocationPermissionRationale
import com.codeancy.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel(),
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    ObserverAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ActiveRunEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            ActiveRunEvent.RunSaved -> {
                onFinish()
            }
        }
    }

    ActiveRunScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ActiveRunAction.OnBackClick -> {
                    if (!viewModel.state.hasStartedRunning) {
                        onBack()
                    }
                }

                else -> viewModel.onAction(action)
            }
        },
        onServiceToggle = onServiceToggle
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->

        val hasCoarseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasAccessFineLocationPermission =
            perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationsPermission =
            if (Build.VERSION.SDK_INT >= 33) perms[Manifest.permission.POST_NOTIFICATIONS] == true else true

        val activity = context as ComponentActivity

        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasAccessFineLocationPermission && hasCoarseLocationPermission,
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationsPermission,
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

    }

    LaunchedEffect(key1 = true) {

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

        if (!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRuniquePermission(context)
        }

    }

    LaunchedEffect(key1 = state.isRunFinished) {
        if (state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    LaunchedEffect(key1 = state.shouldTrack) {
        if (state.shouldTrack && context.hasLocationPermission() && !ActiveRunService.isServiceActive) {
            onServiceToggle(true)
        }
    }



    RuniqueScaffold(withGradient = false, topAppBar = {
        RuniqueToolbar(showBackButton = true,
            title = stringResource(id = R.string.active_run),
            onBackClick = {
                onAction(ActiveRunAction.OnBackClick)
            })
    }, floatingActionButton = {
        RuniqueFloatingActionButton(
            icon = if (state.shouldTrack) StopIcon else StartIcon,
            onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
            iconSize = 20.dp,
            contentDescription = stringResource(id = if (state.shouldTrack) R.string.pause_run else R.string.start_run)
        )
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {

            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapShot = { bmp ->
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bmp.compress(
                            Bitmap.CompressFormat.JPEG,
                            80,
                            it
                        )
                    }
                    onAction(ActiveRunAction.OnRunProcessed(stream.toByteArray()))
                },
                modifier = Modifier.fillMaxSize()
            )

            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues)
                    .fillMaxWidth()
            )

        }

    }

    if (!state.shouldTrack && state.hasStartedRunning) {
        RuniqueDialog(
            title = stringResource(id = R.string.running_is_paused),
            onDismiss = { onAction(ActiveRunAction.OnResumeRunClick) },
            description = stringResource(id = R.string.resume_or_finish_run),
            primaryButton = {
                RuniqueActionButton(
                    text = stringResource(id = R.string.resume_run),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            },
            secondaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.finish_run),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(ActiveRunAction.OnFinishRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {

        RuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            onDismiss = { /* normal dismissing not allowed */ },
            description = stringResource(
                id = when {
                    state.showNotificationRationale && state.showLocationRationale -> R.string.location_notification_rationale
                    state.showLocationRationale -> R.string.location_rationale
                    else -> R.string.notification_rationale
                }
            ),
            primaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRuniquePermission(context)
                    }
                )
            }
        )

    }

}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermission(
    context: Context
) {

    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val notificationPermission =
        if (Build.VERSION.SDK_INT >= 33) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermission)
        }

        !hasLocationPermission -> {
            launch(locationPermissions)
        }

        !hasNotificationPermission -> {
            launch(notificationPermission)
        }
    }

}


@Preview
@Composable
private fun ActiveRunScreenPreview() {

    RuniqueTheme {
        ActiveRunScreen(state = ActiveRunState(), {}) {

        }
    }

}