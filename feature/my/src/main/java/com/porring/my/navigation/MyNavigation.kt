package com.porring.my.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.porring.my.MyRoute
import com.porring.navigation.MainMenuRoute

fun NavController.navigateMy(navOptions: NavOptions) {
    navigate(MainMenuRoute.My, navOptions)
}

fun NavGraphBuilder.myNavGraph(
    padding: PaddingValues,
) {
    composable<MainMenuRoute.My> {
        MyRoute(
            padding = padding
        )
    }
}