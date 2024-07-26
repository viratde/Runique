package com.codeancy.auth.presentation.login

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

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()

    val events = eventChannel.receiveAsFlow()

    fun onAction(event: LoginAction) {

        when (event) {
            is LoginAction.OnEmailChange -> {
                state = state.copy(
                    email = event.value,
                    canLogIn = userDataValidator.isValidEmail(event.value) && state.password.isNotBlank()
                )
            }

            LoginAction.OnLoginClick -> {
                login()
            }

            is LoginAction.OnPasswordChange -> {
                state = state.copy(
                    password = event.value,
                    canLogIn = userDataValidator.isValidEmail(state.email) && event.value.isNotBlank()
                )
            }

            LoginAction.OnRegisterClick -> Unit
            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible,
                )
            }
        }

    }

    private fun login() {
        viewModelScope.launch {

            state = state.copy(isLoggingIn = true)
            val result = authRepository.login(state.email, state.password)
            state = state.copy(isLoggingIn = false)


            when (result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(
                            LoginEvent.Error(UiText.StringResource(R.string.error_email_password_incorrect))
                        )
                    } else {
                        eventChannel.send(
                            LoginEvent.Error(result.error.asUiText())
                        )
                    }
                }

                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }

        }
    }

}