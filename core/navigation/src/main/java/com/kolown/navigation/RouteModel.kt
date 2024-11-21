package com.kolown.navigation

import com.kolown.model.PostContentModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed interface Route {
    @Serializable
    data class Upload(val imgUri: String): MainMenuRoute
}

sealed interface MainMenuRoute: Route {
    @Serializable
    data object Home: MainMenuRoute

    @Serializable
    data object Search: MainMenuRoute

    @Serializable
    data object Camera: MainMenuRoute

    @Serializable
    data object Follower: MainMenuRoute

    @Serializable
    data object My: MainMenuRoute
}


sealed interface AppRoute: Route {
    @Serializable
    data class Detail(val postContentModel : PostContentModel) : AppRoute
}