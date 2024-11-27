package com.kolown.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kolown.login.LoginRoute
import com.kolown.navigation.Route

fun NavController.navigateLogin() {
    navigate(Route.Login)
}

fun NavGraphBuilder.loginNavGraph(
    updateLoginState: () -> Unit,
    popBackStack: () -> Unit,
    navigateToJoin: () -> Unit,
    padding: PaddingValues,
) {
    composable<Route.Login> {
        LoginRoute(
            updateLoginState = updateLoginState,
            popBackStack = popBackStack,
            navigateToJoin = navigateToJoin,
            padding = padding,
        )
    }
}
