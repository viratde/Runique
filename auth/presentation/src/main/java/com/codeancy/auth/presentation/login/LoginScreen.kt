package com.codeancy.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeancy.auth.presentation.R
import com.codeancy.auth.presentation.register.RegisterAction
import com.codeancy.core.presentation.design_system.EmailIcon
import com.codeancy.core.presentation.design_system.Poppins
import com.codeancy.core.presentation.design_system.RuniqueGray
import com.codeancy.core.presentation.design_system.RuniqueTheme
import com.codeancy.core.presentation.design_system.components.GradientBackground
import com.codeancy.core.presentation.design_system.components.RuniqueActionButton
import com.codeancy.core.presentation.design_system.components.RuniquePasswordTextField
import com.codeancy.core.presentation.design_system.components.RuniqueTextField
import com.codeancy.core.presentation.ui.ObserverAsEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current


    ObserverAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(context, R.string.you_are_logged_in, Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> {
                    onSignUpClick()
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )

}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {

    GradientBackground {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)
        ) {

            Text(
                text = stringResource(id = R.string.hi_there),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = stringResource(id = R.string.runique_welcome_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))


            RuniqueTextField(
                value = state.email,
                onValueChange = {
                    onAction(LoginAction.OnEmailChange(it))
                },
                startIcon = EmailIcon,
                endIcon = null,
                hint = stringResource(id = R.string.example_email),
                title = stringResource(id = R.string.email),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))


            RuniquePasswordTextField(
                value = state.password,
                onValueChange = {
                    onAction(LoginAction.OnPasswordChange(it))
                },
                hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth(),
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RuniqueActionButton(
                text = stringResource(id = R.string.login),
                isLoading = state.isLoggingIn,
                enabled = state.canLogIn && !state.isLoggingIn,
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                append(stringResource(id = R.string.dont_have_an_account) + " ")
                pushStringAnnotation(
                    tag = "clickable_text",
                    annotation = stringResource(id = R.string.sign_up)
                )
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(stringResource(id = R.string.sign_up))
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ClickableText(
                text = annotatedString
            ) { offset ->
                annotatedString.getStringAnnotations(
                    tag = "clickable_text",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    onAction(LoginAction.OnRegisterClick)
                }
            }
        }

    }

}

@Preview
@Composable
private fun LoginScreenPreview() {

    RuniqueTheme {
        LoginScreen(state = LoginState()) {

        }
    }
}