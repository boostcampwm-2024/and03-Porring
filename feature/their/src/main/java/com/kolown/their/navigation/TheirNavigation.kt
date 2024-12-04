package com.kolown.their.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import com.kolown.their.DetailTheirRoute
import com.kolown.their.TheirRoute

fun NavController.navigateTheir(authorId: String, navOptions: NavOptions? = null) {
    navigate(MainMenuRoute.Their(authorId), navOptions)
}

fun NavController.navigateTheirDetail(navOptions: NavOptions) {
    navigate(Route.DetailTheir, navOptions)
}

fun NavGraphBuilder.theirNavGraph(
    popBackStack: () -> Unit,
    getBackStackEntry: () -> NavBackStackEntry,
    navigateToDetailTheir: () -> Unit,
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Their> { navBackStackEntry ->
        val parentEntry = remember(navBackStackEntry) {
            getBackStackEntry()
        }
        val followerId = navBackStackEntry.toRoute<MainMenuRoute.Their>().authorId
        TheirRoute(
            popBackStack = popBackStack,
            navigateToDetailTheir = navigateToDetailTheir,
            padding = padding,
            followerId = followerId,
            viewModel = hiltViewModel(parentEntry)
        )
    }

    composable<Route.DetailTheir> {backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            getBackStackEntry()
        }
        DetailTheirRoute(
            padding = padding,
            popBackStack = popBackStack,
            onShowLoginSnackBar = {
                navigateToDetailTheir()
            },
            viewModel = hiltViewModel(parentEntry)
        )
    }
}
