package com.kolown.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kolown.login.util.LoginButton

@Composable
fun LoginButtonGroup(
    vararg buttons: LoginButton,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttons.forEach { button ->
            when (button) {
                is LoginButton.PainterIconButton -> {
                    ButtonWithIcon(
                        icon = button.icon,
                        text = button.text,
                        onClick = button.onClick
                    )
                }

                is LoginButton.VectorIconButton -> {
                    ButtonWithIcon(
                        icon = button.icon,
                        text = button.text,
                        onClick = button.onClick
                    )
                }
            }
        }
    }
}