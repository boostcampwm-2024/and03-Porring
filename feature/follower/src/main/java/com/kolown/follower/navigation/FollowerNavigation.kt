package com.kolown.follower.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.follower.FollowerRoute
import com.porring.navigation.MainMenuRoute

fun NavController.navigateFollower(navOptions: NavOptions) {
    navigate(MainMenuRoute.Follower, navOptions)
}

fun NavGraphBuilder.followerNavGraph(
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Follower> {
        FollowerRoute(
            padding = padding
        )
    }
}
