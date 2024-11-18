package com.kolown.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.internal.composableLambdaN
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.home.HomeRoute
import com.kolown.navigation.MainMenuRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(MainMenuRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    onClickImage: () -> Unit
) {
    composable<MainMenuRoute.Home> {
        HomeRoute(
            padding = padding,
            onClickImage = onClickImage
        )
    }
}
