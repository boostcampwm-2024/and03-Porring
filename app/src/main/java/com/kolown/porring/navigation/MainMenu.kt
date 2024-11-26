package com.kolown.porring.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import com.kolown.porring.R

internal enum class MainMenu(
    @DrawableRes
    val iconResId: Int,
    val contentDescription: String,
    val route: MainMenuRoute,
) {
    HOME(
        iconResId = R.drawable.ic_home_24dp,
        contentDescription = "Home",
        route = MainMenuRoute.Home,
    ),
    SEARCH(
        iconResId = R.drawable.ic_search_24dp,
        contentDescription = "Search",
        route = MainMenuRoute.Search,
    ),
    CAMERA(
        iconResId = R.drawable.ic_add_circle_48dp,
        contentDescription = "Camera",
        route = MainMenuRoute.Camera,
    ),
    FOLLOWER(
        iconResId = R.drawable.ic_follower_24dp,
        contentDescription = "Follow",
        route = MainMenuRoute.Follower,
    ),
    MY(
        iconResId = R.drawable.ic_my_24dp,
        contentDescription = "My",
        route = MainMenuRoute.My,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainMenuRoute) -> Boolean): MainMenu? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries
                .mapNotNull {
                    if (it.route == MainMenuRoute.Camera) null else it.route
                }
                .any { predicate(it) }
        }
    }
}
