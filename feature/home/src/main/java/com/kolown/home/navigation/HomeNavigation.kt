package com.kolown.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.home.HomeRoute
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.navigation.MainMenuRoute
import kotlinx.coroutines.flow.Flow

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(MainMenuRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    mainItems: Flow<List<PostContentModel>>,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    fetchDetailFirst: (PostContentModel) -> Unit,
    updateFollow: (String) -> Unit,
    padding: PaddingValues,
    navigateToTheir: (String) -> Unit,
    navigateToDetail: () -> Unit,
) {
    composable<MainMenuRoute.Home> {
        HomeRoute(
            mainItems = mainItems,
            onSelectReaction = onSelectReaction,
            fetchDetailFirst = fetchDetailFirst,
            updateFollow = updateFollow,
            padding = padding,
            navigateToTheir = navigateToTheir,
            navigateToDetail = navigateToDetail
        )
    }
}
