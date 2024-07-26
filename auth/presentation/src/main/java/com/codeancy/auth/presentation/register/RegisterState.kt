package com.codeancy.auth.presentation.register

import com.codeancy.auth.domain.PasswordValidationState

data class RegisterState constructor(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
){
    val canRegister
        get() = passwordValidationState.isValidPassword && !isRegistering && isEmailValid
}
