package com.kolown.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.designsystem.ui.theme.Surface2

@Composable
fun PorringInputChip(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val textWidth = with(density) {
        textMeasurer.measure(
            text = value.ifEmpty { hint }, style = MaterialTheme.typography.labelLarge
        ).size.width.toDp()
    }

    BasicTextField(
        modifier = modifier.widthIn(max = textWidth + 44.dp).height(32.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        maxLines = 1,
        cursorBrush = SolidColor(Primary),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = MaterialTheme.typography.labelLarge,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.background(
                    color = Surface2, shape = RoundedCornerShape(32.dp)
                ).padding(start = 12.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.let {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }

                Box(
                    modifier = Modifier
                        .width(textWidth),
                    contentAlignment = Alignment.Center
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = PrimaryUnActive,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    innerTextField()
                }

                trailingIcon?.let {
                    Spacer(modifier = Modifier.size(8.dp))
                    trailingIcon()
                }
            }
        },
    )
}