package com.codeancy.auth.presentation.register

import com.codeancy.core.presentation.ui.UiText

sealed interface RegisterEvent {

    data object RegistrationSuccess : RegisterEvent

    data class Error(val error: UiText) : RegisterEvent

}