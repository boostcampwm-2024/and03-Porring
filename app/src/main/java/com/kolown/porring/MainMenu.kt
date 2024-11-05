package com.kolown.porring

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.bongpal.navigation.MainMenuRoute

internal enum class MainMenu(
    @DrawableRes
    val iconResId: Int,
    val contentDescription: String,
    val route: MainMenuRoute,
) {
    HOME(
        iconResId = R.drawable.ic_home_24dp,
        contentDescription = "홈",
        route = MainMenuRoute.Home,
    ),
    SEARCH(
        iconResId = R.drawable.ic_search_24dp,
        contentDescription = "검색",
        route = MainMenuRoute.Search,
    ),
    CAMERA(
        iconResId = R.drawable.ic_add_circle_48dp,
        contentDescription = "촬영",
        route = MainMenuRoute.Camera,
    ),
    FOLLOWER(
        iconResId = R.drawable.ic_follower_24dp,
        contentDescription = "팔로우",
        route = MainMenuRoute.Home,
    ),
    MY(
        iconResId = R.drawable.ic_my_24dp,
        contentDescription = "마이",
        route = MainMenuRoute.My,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainMenuRoute) -> Boolean): MainMenu? {
            return entries.find { predicate(it.route) }
        }
    }
}