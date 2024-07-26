package com.codeancy.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
    data object OnRegisterClick : LoginAction

    data class OnEmailChange(val value: String) : LoginAction
    data class OnPasswordChange(val value: String) : LoginAction
}