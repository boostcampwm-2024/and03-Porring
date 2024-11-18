package com.kolown.upload.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kolown.navigation.MainMenuRoute
import com.kolown.upload.UploadRoute

fun NavController.navigateUpload(navOptions: NavOptions) {
    navigate(MainMenuRoute.Upload, navOptions)
}

fun NavGraphBuilder.uploadNavGraph(
    padding: PaddingValues
) {
    composable<MainMenuRoute.Upload> {
        UploadRoute(
            padding = padding
        )
    }
}