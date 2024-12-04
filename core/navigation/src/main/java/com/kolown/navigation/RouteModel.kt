package com.kolown.navigation

import com.kolown.model.UploadModel
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data class Upload(val imgUri: String, val uploadModel: UploadModel) : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Setting : Route

    @Serializable
    data object Join : Route

    @Serializable
    data object DetailSearch : Route

    @Serializable
    data object DetailMy : Route

    @Serializable
    data object DetailTheir : Route
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

    @Serializable
    data object Detail : MainMenuRoute

}


