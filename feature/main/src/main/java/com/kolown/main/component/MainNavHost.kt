package com.kolown.main.component

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
import com.kolown.join.navigation.joinNavGraph
import com.kolown.login.navigation.loginNavGraph
import com.kolown.main.navigation.MainMenu
import com.kolown.main.navigation.MainNavigator
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UploadModel
import com.kolown.my.navigation.myNavGraph
import com.kolown.search.navigation.searchNavGraph
import com.kolown.setting.navigation.settingNavGraph
import com.kolown.their.navigation.theirNavGraph
import com.kolown.upload.navigation.uploadNavGraph
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun MainNavHost(
    mainItems: Flow<List<PostContentModel>>,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowLoginSnackBar: () -> Unit,
    detailFirstItem: PostContentModel,
    fetchDetailFirst: (PostContentModel) -> Unit,
    updateFollow: (String) -> Unit,
    isLoggedIn: Boolean,
    updateLoginState: () -> Unit,
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues,
    uploadPost: (String, String, List<String>) -> Unit,
    updateMainItems: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            homeNavGraph(
                isLoggedIn = isLoggedIn,
                onShowLoginSnackBar = onShowLoginSnackBar,
                mainItems = mainItems,
                onSelectReaction = onSelectReaction,
                fetchDetailFirst = fetchDetailFirst,
                updateFollow = updateFollow,
                padding = padding,
                navigateToTheir = navigator::navigateToTheir,
                navigateToDetail = navigator::navigateToDetail,
                updateMainItems = updateMainItems
            )

            searchNavGraph(
                isLoggedIn = isLoggedIn,
                onShowLoginSnackBar = onShowLoginSnackBar,
                padding = padding,
                navigateToTheir =  { id ->
                    navigator.navigateToTheir(id)
                },
                navigateToSearchDetail = navigator::navigateToDetailSearch,
                popBackStack = navigator::popBackStack,
                getBackStackEntry = { navigator.navController.getBackStackEntry(MainMenu.SEARCH.route) }
            )

            cameraNavGraph(
                navigateToUpload = { imgUri ->
                    navigator.navigateToUpload(
                        imgUri,
                        UploadModel("", "", emptyList())
                    )
                },
                padding = padding,
                popBackStack = navigator::popBackStack
            )

            followerNavGraph(
                isLoggedIn = isLoggedIn,
                navigateToLogin = navigator::navigateToLogin,
                navigateToTheir = navigator::navigateToTheir,
                padding = padding
            )

            myNavGraph(
                isLoggedIn = isLoggedIn,
                navigateToLogin = navigator::navigateToLogin,
                navigateToSetting = navigator::navigateToSetting,
                padding = padding
            )

            detailNavGraph(
                isLoggedIn = isLoggedIn,
                onShowLoginSnackBar = onShowLoginSnackBar,
                detailFirstItem = detailFirstItem,
                updateMainPostReaction = onSelectReaction,
                updateFollow = updateFollow,
                popBackStack = navigator::popBackStack,
                padding = padding,
                navigateToTheir = navigator::navigateToTheir
            )

            uploadNavGraph(
                navigateToHome = { navigator.navigate(MainMenu.HOME) },
                uploadPost = uploadPost,
                padding = padding
            )

            loginNavGraph(
                updateLoginState = updateLoginState,
                popBackStack = navigator::popBackStack,
                onShowSnackBar = onShowSnackBar,
                navigateToJoin = navigator::navigateToJoin,
                padding = padding
            )

            settingNavGraph(
                popBackStack = navigator::popBackStack,
                updateLoginState = updateLoginState,
                padding = padding
            )

            theirNavGraph(
                popBackStack = navigator::popBackStack,
                padding = padding
            )

            joinNavGraph(
                onShowSnackBar = onShowSnackBar,
                popBackStack = navigator::popBackStack,
                padding = padding
            )
        }
    }
}
