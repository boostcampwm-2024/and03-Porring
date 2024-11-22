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
import com.kolown.camera.navigation.navigateCamera
import com.kolown.detail.navigation.navigateToDetail
import com.kolown.follower.navigation.navigateFollower
import com.kolown.home.navigation.navigateHome
import com.kolown.login.navigation.navigateLogin
import com.kolown.model.PostContentModel
import com.kolown.my.navigation.navigateMy
import com.kolown.search.navigation.navigateSearch
import com.kolown.setting.navigation.navigateSetting
import com.kolown.upload.navigation.navigateUpload

internal class MainNavigator(
    val navController: NavHostController,
) {
    val startDestination = MainMenu.HOME.route
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination
    val currentMenu: MainMenu?
        @Composable get() = MainMenu.find { m ->
            currentDestination?.hasRoute(m::class) == true
        }

    fun navigate(menu: MainMenu) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (menu) {
            MainMenu.HOME -> navController.navigateHome(navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            })

            MainMenu.SEARCH -> navController.navigateSearch(navOptions)
            MainMenu.CAMERA -> navController.navigateCamera(navOptions)
            MainMenu.FOLLOWER -> navController.navigateFollower(navOptions)
            MainMenu.MY -> navController.navigateMy(navOptions)
        }
    }

    fun navigateToUpload(imgUri: String) = navController.navigateUpload(imgUri)

    fun navigateToDetail(postContentModel: PostContentModel) =
        navController.navigateToDetail(postContentModel)

    fun navigateToLogin() = navController.navigateLogin()

    fun navigateToSetting() = navController.navigateSetting()

    fun popBackStack() {
        navController.popBackStack()
    }

    @Composable
    fun isShowBottomBar() = MainMenu.contains {
        currentDestination?.hasRoute(it::class) == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
