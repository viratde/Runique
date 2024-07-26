package com.codeancy.run.presentation.active_run

sealed interface ActiveRunAction {

    data object OnToggleRunClick : ActiveRunAction

    data object OnFinishRunClick : ActiveRunAction

    data object OnResumeRunClick : ActiveRunAction

    data object OnBackClick : ActiveRunAction

    class OnRunProcessed(
        val mapPictureByte: ByteArray
    ) : ActiveRunAction

    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationPermissionRationale: Boolean
    ) : ActiveRunAction


    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ) : ActiveRunAction

    data object DismissRationaleDialog : ActiveRunAction
}