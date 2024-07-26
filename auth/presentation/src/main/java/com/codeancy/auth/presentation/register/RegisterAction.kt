package com.codeancy.auth.presentation.register

sealed interface RegisterAction {

    data object OnTogglePasswordVisibility : RegisterAction
    data object OnLoginClick : RegisterAction
    data object OnRegisterClick : RegisterAction

    data class OnEmailChange(val value: String) : RegisterAction
    data class OnPasswordChange(val value: String) : RegisterAction
}