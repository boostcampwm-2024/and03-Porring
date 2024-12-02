package com.kolown.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.ui.theme.Error
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryDark
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.designsystem.ui.theme.PrimaryUnActiveDark
import com.kolown.designsystem.ui.theme.Surface
import com.kolown.designsystem.ui.theme.SurfaceError

@Composable
fun PorringTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    label: String? = null,
    validator: ((String) -> Boolean)? = null,
    onErrorChange: (Boolean) -> Unit = {},
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLine: Int = 1,
    modifier: Modifier = Modifier,
) {
    var isError by remember { mutableStateOf(false) }
    var isInitial by remember { mutableStateOf(false) }
    var maxHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(isError) { onErrorChange(isError) }

    BasicTextField(
        modifier = modifier.fillMaxWidth().onFocusChanged {
            if (isInitial) {
                validator?.let { validate ->
                    if (it.hasFocus.not()) {
                        isError = validate(value)
                    }
                }
            } else {
                isInitial = true
            }
        },
        value = value,
        onValueChange = {
            onValueChange(it)
            isError = false
        },
        maxLines = maxLine,
        cursorBrush = SolidColor(Primary),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onTextLayout = {
            maxHeight = (it.lineCount * 56).dp
        },
        visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = MaterialTheme.typography.bodyLarge,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = maxHeight)
                    .border(
                        width = if (isError) 2.dp else 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = if (isError) Error else PrimaryDark
                    )
                    .background(
                        color = if (isError) SurfaceError else Surface,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.let {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) Error else PrimaryDark
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                }

                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        hint?.let { h ->
                            Text(
                                text = h,
                                color = if (isError) Error else PrimaryUnActive,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        label?.let { l ->
                            AnimatedVisibility(
                                visible = value.isNotEmpty(),
                                enter = slideInVertically() + fadeIn(),
                                exit = ExitTransition.None
                            ) {
                                Text(
                                    text = l,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isError) Error else PrimaryUnActiveDark
                                )
                            }
                        }
                        innerTextField()
                    }
                }
                trailingIcon?.let {
                    Spacer(modifier = Modifier.size(16.dp))
                    trailingIcon()
                }
            }
        },
    )
}