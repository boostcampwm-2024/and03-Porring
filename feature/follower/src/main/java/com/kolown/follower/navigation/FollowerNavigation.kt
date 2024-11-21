package com.kolown.follower.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.follower.FollowerRoute
import com.kolown.navigation.MainMenuRoute

fun NavController.navigateFollower(navOptions: NavOptions) {
    navigate(MainMenuRoute.Follower, navOptions)
}

fun NavGraphBuilder.followerNavGraph(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Follower> {
        FollowerRoute(
            isLoggedIn = isLoggedIn,
            navigateToLogin = navigateToLogin,
            padding = padding
        )
    }
}
