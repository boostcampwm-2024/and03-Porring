package com.kolown.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.navigation.Route
import com.kolown.setting.SettingRoute

fun NavController.navigateSetting(navOptions: NavOptions) {
    navigate(Route.Setting,navOptions = navOptions)
}

fun NavGraphBuilder.settingNavGraph(
    popBackStack: () -> Unit,
    updateLoginState: () -> Unit,
    padding: PaddingValues,
) {
    composable<Route.Setting> {
        SettingRoute(
            popBackStack = popBackStack,
            updateLoginState = updateLoginState,
            padding = padding
        )
    }
}