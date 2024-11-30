package com.kolown.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.kolown.login.LoginRoute
import com.kolown.navigation.Route

fun NavController.navigateLogin() {
    navigate(
        route = Route.Login,
        navOptions = navOptions { launchSingleTop = true }
    )
}

fun NavGraphBuilder.loginNavGraph(
    updateLoginState: () -> Unit,
    popBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    navigateToJoin: () -> Unit,
    padding: PaddingValues,
) {
    composable<Route.Login> {
        LoginRoute(
            updateLoginState = updateLoginState,
            popBackStack = popBackStack,
            onShowSnackBar = onShowSnackBar,
            navigateToJoin = navigateToJoin,
            padding = padding,
        )
    }
}
