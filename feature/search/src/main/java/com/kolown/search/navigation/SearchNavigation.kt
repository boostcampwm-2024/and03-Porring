package com.kolown.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.porring.navigation.MainMenuRoute
import com.kolown.search.SearchRoute

fun NavController.navigateSearch(navOptions: NavOptions) {
    navigate(MainMenuRoute.Search, navOptions)
}

fun NavGraphBuilder.searchNavGraph(
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Search> {
        SearchRoute(
            padding = padding
        )
    }
}
