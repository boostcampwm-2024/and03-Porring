package com.kolown.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kolown.designsystem.ui.theme.PorringTheme
import com.kolown.main.navigation.MainNavigator
import com.kolown.main.navigation.rememberMainNavigator
import com.kolown.navigation.AppRoute
import com.kolown.navigation.MainMenuRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigator: MainNavigator = rememberMainNavigator()
            var isLightBars by remember { mutableStateOf(false) }
            var currentRoute by remember { mutableStateOf("") }

            currentRoute = navigator.currentDestination?.route?.substringAfterLast(".").orEmpty()
            isLightBars = when (currentRoute) {
                AppRoute.Detail.toString() -> false
                MainMenuRoute.Camera.toString() -> false
                else -> true
            }

            PorringTheme(isLightBars = isLightBars) {
                MainScreen(
                    navigator = navigator
                )
            }
        }
    }
}