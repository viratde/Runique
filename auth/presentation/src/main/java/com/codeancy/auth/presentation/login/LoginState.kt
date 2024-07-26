package com.codeancy.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val canLogIn: Boolean = false,
    val isLoggingIn: Boolean = false
)
