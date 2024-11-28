package com.kolown.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kolown.main.navigation.MainNavigator
import com.kolown.main.navigation.rememberMainNavigator
import com.kolown.main.ui.theme.PorringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            PorringTheme {
                MainScreen(
                    navigator = navigator
                )
            }
        }
    }
}