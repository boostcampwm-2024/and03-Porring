package com.kolown.login.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
internal fun LoginButtonGroup(
    vararg buttons: LoginButton,
    visible: Boolean = true,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
}