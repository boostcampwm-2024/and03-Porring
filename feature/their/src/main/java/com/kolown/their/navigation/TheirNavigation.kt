package com.kolown.their.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.navigation.MainMenuRoute
import com.kolown.their.TheirRoute

fun NavController.navigateTheir(authorId: String, navOptions: NavOptions? = null) {
    navigate(MainMenuRoute.Their(authorId), navOptions)
}

fun NavGraphBuilder.theirNavGraph(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Their> {
        TheirRoute(
            isLoggedIn = isLoggedIn,
            navigateToLogin = navigateToLogin,
            padding = padding
        )
    }
}
