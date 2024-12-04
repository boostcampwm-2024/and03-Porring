package com.kolown.my.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.my.DetailMyRoute
import com.kolown.my.MyRoute
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route

fun NavController.navigateMy(navOptions: NavOptions) {
    navigate(MainMenuRoute.My, navOptions)
}

fun NavController.navigateMyDetail(navOptions: NavOptions) {
    navigate(Route.DetailMy, navOptions)
}

fun NavGraphBuilder.myNavGraph(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToDetailMy: () -> Unit,
    padding: PaddingValues,
    popBackStack: () -> Unit,
    getBackStackEntry: () -> NavBackStackEntry
) {
    composable<MainMenuRoute.My> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            getBackStackEntry()
        }
        MyRoute(
            isLoggedIn = isLoggedIn,
            navigateToLogin = navigateToLogin,
            navigateToSetting = navigateToSetting,
            navigateToDetailMy = navigateToDetailMy,
            padding = padding,
            viewModel = hiltViewModel(parentEntry)
        )
    }

    composable<Route.DetailMy> {backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            getBackStackEntry()
        }
        DetailMyRoute(
            padding = padding,
            popBackStack = popBackStack,
            onShowLoginSnackBar = {
                navigateToLogin()
            },
            viewModel = hiltViewModel(parentEntry)
        )
    }
}
