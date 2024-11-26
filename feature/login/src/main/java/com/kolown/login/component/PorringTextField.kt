package com.kolown.login.component

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.Primary
import com.kolown.designsystem.PrimaryDark
import com.kolown.designsystem.PrimaryUnActive
import com.kolown.designsystem.PrimaryUnActiveDark
import com.kolown.designsystem.Surface

@Composable
fun PorringTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String? = null,
    label: String? = null,
    leadingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        modifier = modifier
            .height(56.dp),
        value = value,
        onValueChange = onValueChange,
        maxLines = 1,
        cursorBrush = SolidColor(Primary),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerText ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp),
                        color = PrimaryDark
                    )
                    .background(Surface, RoundedCornerShape(10.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = PrimaryDark
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        hint?.let { h ->
                            Text(
                                text = h,
                                color = PrimaryUnActive,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                                    color = PrimaryUnActiveDark
                                )
                            }
                        }
                        innerText()
                    }
                }
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge
    )
}