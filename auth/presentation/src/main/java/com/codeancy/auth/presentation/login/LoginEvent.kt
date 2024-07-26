package com.codeancy.auth.presentation.login

import com.codeancy.core.presentation.ui.UiText

sealed interface LoginEvent {

    data object LoginSuccess : LoginEvent

    data class Error(val error: UiText) : LoginEvent

}