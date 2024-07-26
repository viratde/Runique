package com.codeancy.core.presentation.design_system.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeancy.core.presentation.design_system.CheckIcon
import com.codeancy.core.presentation.design_system.EmailIcon
import com.codeancy.core.presentation.design_system.EyeClosedIcon
import com.codeancy.core.presentation.design_system.EyeOpenedIcon
import com.codeancy.core.presentation.design_system.LockIcon
import com.codeancy.core.presentation.design_system.R
import com.codeancy.core.presentation.design_system.RuniqueTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RuniquePasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    title: String?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {

    var isFocused by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            if (title != null) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        BasicSecureTextField(
            value = value,
            onValueChange = onValueChange,
            textObfuscationMode = if (isPasswordVisible) TextObfuscationMode.Visible else TextObfuscationMode.Hidden,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            keyboardType = KeyboardType.Password,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 1.dp,
                    if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                    RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            decorator = { innerBox ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = LockIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.4f
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        innerBox()

                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = onTogglePasswordVisibility,
                    ) {
                        Icon(
                            imageVector = if (!isPasswordVisible) EyeClosedIcon else EyeOpenedIcon,
                            contentDescription = stringResource(id = if (isPasswordVisible) R.string.show_password else R.string.hide_password),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                }

            }
        )
    }

}

@Preview
@Composable
private fun RuniquePasswordTextFieldPreview() {
    RuniqueTheme {
        RuniquePasswordTextField(
            value = "vk12@gmail.com",
            onValueChange = {},
            hint = "example@test.com",
            title = "Password",
            modifier = Modifier.fillMaxWidth(),
            isPasswordVisible = false,
            onTogglePasswordVisibility = {

            }
        )
    }
}