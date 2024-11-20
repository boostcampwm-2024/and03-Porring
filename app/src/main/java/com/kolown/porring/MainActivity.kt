package com.kolown.porring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kolown.porring.ui.theme.PorringTheme
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