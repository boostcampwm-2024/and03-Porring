package com.kolown.join.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kolown.join.JoinRoute
import com.kolown.navigation.Route

fun NavController.navigateToJoin() {
    navigate(Route.Join)
}

fun NavGraphBuilder.joinNavGraph(
    onShowSnackBar: (String) -> Unit,
    popBackStack: () -> Unit,
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
