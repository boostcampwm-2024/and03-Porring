package com.kolown.porring.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.kolown.detail.navigation.detailNavGraph
import com.kolown.porring.MainNavigator
import com.kolown.camera.navigation.cameraNavGraph
import com.kolown.follower.navigation.followerNavGraph
import com.kolown.home.navigation.homeNavGraph
import com.kolown.my.navigation.myNavGraph
import com.kolown.search.navigation.searchNavGraph

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues,
) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            homeNavGraph(
                padding = padding,
                onClickImage = { navigator.navigateToDetail() }
            )

            searchNavGraph(
                padding = padding
            )

            cameraNavGraph(
                padding = padding
            )

            followerNavGraph(
                padding = padding
            )

            myNavGraph(
                padding = padding
            )

            detailNavGraph(
                padding = padding
            )
        }
    }
}
