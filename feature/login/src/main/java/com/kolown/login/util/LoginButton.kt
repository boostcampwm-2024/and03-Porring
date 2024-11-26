package com.kolown.login.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class LoginButton {
    data class PainterIconButton(
        @DrawableRes val icon: Int,
        val text: String,
        val onClick: () -> Unit,
    ) : LoginButton()

    data class VectorIconButton(
        val icon: ImageVector,
        val text: String,
        val onClick: () -> Unit,
    ) : LoginButton()
}