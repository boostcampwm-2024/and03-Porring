package com.kolown.porring

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.bongpal.home.navigation.navigateHome

internal class MainNavigator(
    val navController: NavHostController,
) {
    val startDestination = MainMenu.HOME.route
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination
    val currentMenu: MainMenu?
        @Composable get() = MainMenu.find { m -> currentDestination?.hasRoute(m::class) == true }

    fun navigate(menu: MainMenu) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (menu) {
            MainMenu.HOME -> {
                navController.navigateHome(navOptions)
            }

            MainMenu.SEARCH -> TODO()
            MainMenu.CAMERA -> TODO()
            MainMenu.FOLLOWER -> TODO()
            MainMenu.MY -> TODO()
        }
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}