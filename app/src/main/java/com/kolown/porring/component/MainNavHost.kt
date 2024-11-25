package com.kolown.porring.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import com.kolown.camera.navigation.cameraNavGraph
import com.kolown.detail.navigation.detailNavGraph
import com.kolown.follower.navigation.followerNavGraph
import com.kolown.home.navigation.homeNavGraph
import com.kolown.login.navigation.loginNavGraph
import com.kolown.my.navigation.myNavGraph
import com.kolown.porring.MainMenu
import com.kolown.porring.MainNavigator
import com.kolown.search.navigation.searchNavGraph
import com.kolown.setting.navigation.settingNavGraph
import com.kolown.their.navigation.theirNavGraph
import com.kolown.upload.navigation.uploadNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun MainNavHost(
    isLoggedIn: Boolean,
    updateLoginState: () -> Unit,
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues,
) {
    Box(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            homeNavGraph(
                padding = padding,
                onClickImage = { postContentModel ->
                    navigator.navigateToDetail(postContentModel)
                }
            )

            searchNavGraph(
                padding = padding
            )

            cameraNavGraph(
                navigateToUpload = { imgUri -> navigator.navigateToUpload(imgUri) },
                padding = padding
            )

            followerNavGraph(
                isLoggedIn = isLoggedIn,
                navigateToLogin = navigator::navigateToLogin,
                padding = padding
            )

            myNavGraph(
                isLoggedIn = isLoggedIn,
                navigateToLogin = navigator::navigateToLogin,
                navigateToSetting = navigator::navigateToSetting,
                padding = padding
            )

            detailNavGraph(
                padding = padding
            )

            uploadNavGraph(
                navigateToHome = { navigator.navigate(MainMenu.HOME) },
                padding = padding
            )

            loginNavGraph(
                updateLoginState = updateLoginState,
                popBackStack = navigator::popBackStack,
                padding = padding
            )

            settingNavGraph(
                popBackStack = navigator::popBackStack,
                updateLoginState = updateLoginState,
                padding = padding
            )

            theirNavGraph(
                isLoggedIn = isLoggedIn,
                navigateToLogin = navigator::navigateToLogin,
                popBackStack = navigator::popBackStack,
                padding = padding
            )
        }
    }
}
