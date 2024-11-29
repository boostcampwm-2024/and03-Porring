package com.kolown.designsystem.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    background = Background,
    surface = Surface,

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PorringTheme(
    isLightBars: Boolean,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        else -> LightColorScheme
    }

    SideEffect {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        insetsController.isAppearanceLightStatusBars = isLightBars
        insetsController.isAppearanceLightNavigationBars = isLightBars
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor =
            if (isLightBars) Background.toArgb() else BackgroundDark.toArgb()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
