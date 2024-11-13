package com.kolown.camera.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.porring.camera.CameraRoute
import com.porring.navigation.MainMenuRoute

fun NavController.navigateCamera(navOptions: NavOptions) {
    navigate(MainMenuRoute.Camera, navOptions)
}

fun NavGraphBuilder.cameraNavGraph(
    padding: PaddingValues,
) {
    composable<MainMenuRoute.Camera> {
        CameraRoute(
            padding = padding
        )
    }
}
