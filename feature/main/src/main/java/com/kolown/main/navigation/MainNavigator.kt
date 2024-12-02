package com.kolown.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.kolown.camera.navigation.navigateCamera
import com.kolown.detail.navigation.navigateToDetail
import com.kolown.follower.navigation.navigateFollower
import com.kolown.home.navigation.navigateHome
import com.kolown.join.navigation.navigateToJoin
import com.kolown.login.navigation.navigateLogin
import com.kolown.model.UploadModel
import com.kolown.my.navigation.navigateMy
import com.kolown.navigation.MainMenuRoute
import com.kolown.navigation.Route
import com.kolown.search.navigation.navigateSearch
import com.kolown.search.navigation.navigateSearchDetail
import com.kolown.setting.navigation.navigateSetting
import com.kolown.their.navigation.navigateTheir
import com.kolown.upload.navigation.navigateUpload

internal class MainNavigator(
    val navController: NavHostController,
) {
    val startDestination = MainMenu.HOME.route
    internal val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination
    val currentMenu: MainMenu?
        @Composable get() = MainMenu.find { m ->
            currentDestination?.hasRoute(m::class) == true
        }
    private val singleTopOptions = navOptions {
        launchSingleTop = true
        restoreState = true
    }

    fun navigate(menu: MainMenu) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = menu == MainMenu.HOME
            }
            launchSingleTop = true
        }

        when (menu) {
            MainMenu.HOME -> navController.navigateHome(navOptions)
            MainMenu.SEARCH -> navController.navigateSearch(navOptions)
            MainMenu.CAMERA -> navController.navigateCamera(navOptions)
            MainMenu.FOLLOWER -> navController.navigateFollower(navOptions)
            MainMenu.MY -> navController.navigateMy(navOptions)
        }
    }

    fun navigateToTheir(authorId: String) = navController.navigateTheir(authorId = authorId,navOptions = singleTopOptions)

    fun navigateToUpload(imgUri: String, uploadModel: UploadModel) =
        navController.navigateUpload(imgUri, uploadModel)

    fun navigateToDetail() = navController.navigateToDetail(navOptions  = singleTopOptions)

    fun navigateToLogin() = navController.navigateLogin(navOptions = singleTopOptions)

    fun navigateToSetting() = navController.navigateSetting(navOptions = singleTopOptions)

    fun navigateToJoin() = navController.navigateToJoin(navOptions  = singleTopOptions)

    fun navigateToDetailSearch() = navController.navigateSearchDetail(navOptions =singleTopOptions)

    fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStack(destination: Route) {
        navController.popBackStack(destination, false)
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
