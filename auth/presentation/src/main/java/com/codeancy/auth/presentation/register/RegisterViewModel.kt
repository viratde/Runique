package com.codeancy.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeancy.auth.domain.AuthRepository
import com.codeancy.auth.domain.UserDataValidator
import com.codeancy.auth.presentation.R
import com.codeancy.core.domain.util.DataError
import com.codeancy.core.domain.util.Result
import com.codeancy.core.presentation.ui.UiText
import com.codeancy.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()

    val events = eventChannel.receiveAsFlow()

    init {

    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnPasswordChange -> {
                state = state.copy(
                    password = action.value,
                    passwordValidationState = userDataValidator.validatePassword(action.value)
                )
            }

            is RegisterAction.OnEmailChange -> {
                state = state.copy(
                    email = action.value,
                    isEmailValid = userDataValidator.isValidEmail(action.value)
                )
            }

            RegisterAction.OnRegisterClick -> {
                register()
            }

            RegisterAction.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            else -> Unit
        }
    }

    private fun register() {
        viewModelScope.launch {

            state = state.copy(isRegistering = true)

            val result = authRepository.register(
                state.email.trim(),
                state.password
            )

            state = state.copy(isRegistering = false)

            when (result) {

                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        eventChannel.send(
                            RegisterEvent.Error(
                                error = UiText.StringResource(
                                    R.string.error_email_exists
                                )
                            )
                        )
                    } else {
                        eventChannel.send(RegisterEvent.Error(error = result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }

            }
        }
    }

}