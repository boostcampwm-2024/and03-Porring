package com.kolown.camera.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.camera.screen.CameraRoute
import com.kolown.navigation.MainMenuRoute

fun NavController.navigateCamera(navOptions: NavOptions) {
    navigate(MainMenuRoute.Camera, navOptions)
}

fun NavGraphBuilder.cameraNavGraph(
    navigateToUpload: (String) -> Unit = {},
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Camera> {
        CameraRoute(
            navigateToUpload = navigateToUpload,
            padding = padding
        )
    }
}
