package com.kolown.upload.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import com.kolown.upload.UploadRoute

fun NavController.navigateUpload(imgUri: String, navOptions: NavOptions? = null) {
    navigate(Route.Upload(imgUri), navOptions)
}

fun NavGraphBuilder.uploadNavGraph(
    navigateToHome: () -> Unit,
    padding: PaddingValues
) {
    composable<Route.Upload> { navBackStackEntry ->
        val imgUri = navBackStackEntry.toRoute<Route.Upload>().imgUri

        UploadRoute(
            imgUri = imgUri,
            padding = padding,
            navigateToHome = navigateToHome
        )
    }
}