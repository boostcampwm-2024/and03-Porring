package com.porring.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.porring.home.HomeRoute
import com.porring.navigation.MainMenuRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(MainMenuRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Home> {
        HomeRoute(
            padding = padding
        )
    }
}