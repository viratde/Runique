package com.codeancy.auth.domain

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasOneNumber: Boolean = false,
    val hasLowerCaseCharacter: Boolean = false,
    val hasUpperCaseCharacter: Boolean = false
) {
    val isValidPassword
        get() = hasMinLength && hasOneNumber && hasUpperCaseCharacter && hasLowerCaseCharacter
}

