package com.kolown.join.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.join.JoinRoute
import com.kolown.navigation.Route

fun NavController.navigateToJoin(navOptions: NavOptions) {
    navigate(Route.Join, navOptions = navOptions)
}

fun NavGraphBuilder.joinNavGraph(
    onShowSnackBar: (String) -> Unit,
    popBackStack: (Route) -> Unit,
    padding: PaddingValues,
) {
    composable<Route.Join> {
        JoinRoute(
            onShowSnackBar = onShowSnackBar,
            popBackStack = popBackStack,
            padding = padding
        )
    }
}
