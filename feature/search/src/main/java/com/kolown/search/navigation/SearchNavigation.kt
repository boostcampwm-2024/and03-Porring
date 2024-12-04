package com.kolown.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import com.kolown.search.DetailSearchRoute
import com.kolown.search.SearchRoute

fun NavController.navigateSearch(navOptions: NavOptions) {
    navigate(MainMenuRoute.Search, navOptions)
}

fun NavController.navigateSearchDetail(navOptions: NavOptions) {
    navigate(Route.DetailSearch, navOptions = navOptions)
}


fun NavGraphBuilder.searchNavGraph(
    padding: PaddingValues,
    navigateToTheir: (String) -> Unit,
    isLoggedIn: Boolean,
    
    navigateToSearchDetail: () -> Unit,
    popBackStack: () -> Unit,
    getBackStackEntry: () -> NavBackStackEntry
) {
    composable<MainMenuRoute.Search> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            getBackStackEntry()
        }
        SearchRoute(
            padding = padding,
            viewModel = hiltViewModel(parentEntry),
            navigateToSearchDetail = navigateToSearchDetail
        )
    }

    composable<Route.DetailSearch> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            getBackStackEntry()
        }
        DetailSearchRoute(
            padding = padding,
            viewModel = hiltViewModel(parentEntry),
            navigateToTheir = navigateToTheir,
            popBackStack = popBackStack,
            isLoggedIn = isLoggedIn,
        )
    }
}
