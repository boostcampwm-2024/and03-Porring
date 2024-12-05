package com.kolown.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kolown.common.component.LocalSnackBarBridge
import com.kolown.common.component.SnackBarBridge
import com.kolown.designsystem.ui.theme.PorringTheme
import com.kolown.main.component.NotAvailableVersionScreen
import com.kolown.main.navigation.MainNavigator
import com.kolown.main.navigation.rememberMainNavigator
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigator: MainNavigator = rememberMainNavigator()
            var isLightBars by remember { mutableStateOf(false) }

            val currentRoute =
                navigator.currentDestination?.route?.substringAfterLast(".").orEmpty()
            isLightBars = when (currentRoute) {
                MainMenuRoute.Detail.toString() -> false
                MainMenuRoute.Camera.toString() -> false
                Route.DetailMy.toString() -> false
                Route.DetailSearch.toString() -> false
                Route.DetailTheir.toString() -> false
                else -> true
            }
            val versionNameState by mainViewModel.versionNameFlow.collectAsState("")

            val vName = this.packageManager.getPackageInfo(this.packageName, 0).versionName
            mainViewModel.getVersionName()
            val snackBarBridge = remember { SnackBarBridge(mainViewModel::postSnackBarData) }
            PorringTheme(isLightBars = isLightBars) {
                if (vName != versionNameState) {
                    NotAvailableVersionScreen(onExitAppButtonClicked = {
                        finishAndRemoveTask()
                    })
                }else{
                    CompositionLocalProvider(LocalSnackBarBridge.provides(snackBarBridge)) {
                        MainScreen(
                            navigator = navigator,
                            mainViewModel
                        )
                    }
                }

            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}
