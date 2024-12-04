package com.kolown.detail.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.detail.DetailRoute
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.navigation.MainMenuRoute

fun NavController.navigateToDetail(navOptions: NavOptions) {
    navigate(MainMenuRoute.Detail, navOptions = navOptions)
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.detailNavGraph(
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    detailFirstItem: PostContentModel,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    popBackStack: () -> Unit,
    padding: PaddingValues,
    navigateToTheir : (String) -> Unit,
    updateFollow: (String) -> Unit
) {
    composable<MainMenuRoute.Detail> {
        DetailRoute(
            isLoggedIn = isLoggedIn,
            onShowLoginSnackBar = onShowLoginSnackBar,
            detailFirstItem = detailFirstItem,
            updateMainPostReaction = updateMainPostReaction,
            popBackStack = popBackStack,
            padding = padding,
            navigateToTheir = navigateToTheir,
            updateFollow = updateFollow
        )
    }
}
