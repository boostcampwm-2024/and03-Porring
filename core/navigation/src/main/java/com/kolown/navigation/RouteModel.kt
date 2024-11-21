package com.kolown.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data class Upload(val imgUri: String): MainMenuRoute
//
//    @Serializable
//    data class Detail(val postContent : PostContentModel) : Route
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
