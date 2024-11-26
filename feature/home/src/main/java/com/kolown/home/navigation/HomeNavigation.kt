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

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(MainMenuRoute.Home, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    mainItems: Result<List<PostContentModel>>,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    fetchDetailFirst: (PostContentModel) -> Unit,
    padding: PaddingValues,
<<<<<<< HEAD
    onClickImage: (PostContentModel) -> Unit,
    navigateToTheir: (String) -> Unit
=======
    navigateToDetail: () -> Unit,
>>>>>>> 399f857 (Refactor: reaction refactoring)
) {
    composable<MainMenuRoute.Home> {
        HomeRoute(
            mainItems = mainItems,
            onSelectReaction = onSelectReaction,
            fetchDetailFirst = fetchDetailFirst,
            padding = padding,
<<<<<<< HEAD
            onClickImage = onClickImage,
            navigateToTheir = navigateToTheir
=======
            navigateToDetail = navigateToDetail
>>>>>>> 399f857 (Refactor: reaction refactoring)
        )
    }
}
