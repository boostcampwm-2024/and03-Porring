package com.kolown.navigation

import com.kolown.model.PostContentModel
import com.kolown.model.UploadModel
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data class Upload(val imgUri: String, val uploadModel: UploadModel) : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Setting : Route
}

sealed interface MainMenuRoute : Route {
    @Serializable
    data object Home : MainMenuRoute

    @Serializable
    data object Search : MainMenuRoute

    @Serializable
    data object Camera : MainMenuRoute

    @Serializable
    data object Follower : MainMenuRoute

    @Serializable
    data object My : MainMenuRoute

    @Serializable
    data class Their(val authorId: String) : MainMenuRoute
}


sealed interface AppRoute : Route {
    @Serializable
    data object Detail : AppRoute
}