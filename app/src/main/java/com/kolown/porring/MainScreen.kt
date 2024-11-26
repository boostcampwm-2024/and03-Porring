package com.kolown.porring

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.porring.component.MainBottomBar
import com.kolown.porring.component.MainNavHost
import com.kolown.porring.navigation.MainMenu
import com.kolown.porring.navigation.MainNavigator
import com.kolown.porring.navigation.rememberMainNavigator
import com.kolown.porring.viewmodel.MainPostItemViewModel
import com.kolown.porring.viewmodel.UserStateViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    userStateViewModel: UserStateViewModel = hiltViewModel(),
    mainPostItemViewModel: MainPostItemViewModel = hiltViewModel(),
) {
    val mainItems by mainPostItemViewModel.mainItems.collectAsStateWithLifecycle()
    val detailFirstItem by mainPostItemViewModel.detailFirstItem.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoggedIn by userStateViewModel.loginState.collectAsStateWithLifecycle()

    MainScreenContent(
        mainItems = mainItems,
        onSelectReaction = mainPostItemViewModel::selectReaction,
        detailFirstItem = detailFirstItem,
        fetchDetailFirst = mainPostItemViewModel::fetchDetailFirst,
        updateFollow = mainPostItemViewModel::updateFollow,
        isLoggedIn = isLoggedIn,
        updateLoginState = userStateViewModel::updateLoginState,
        navigator = navigator,
        snackBarHostState = snackBarHostState,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MainScreenContent(
    mainItems: Flow<List<PostContentModel>>,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    detailFirstItem: PostContentModel,
    fetchDetailFirst: (PostContentModel) -> Unit,
    updateFollow: (String) -> Unit,
    isLoggedIn: Boolean,
    updateLoginState: () -> Unit,
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                mainItems = mainItems,
                onSelectReaction = onSelectReaction,
                detailFirstItem = detailFirstItem,
                fetchDetailFirst = fetchDetailFirst,
                updateFollow = updateFollow,
                isLoggedIn = isLoggedIn,
                updateLoginState = updateLoginState,
                navigator = navigator,
                padding = padding
            )
        },
        bottomBar = {
            MainBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(top = 12.dp, bottom = 16.dp),
                visible = navigator.isShowBottomBar(),
                menus = MainMenu.entries.toPersistentList(),
                currentMenu = navigator.currentMenu,
                onMenuSelected = { navigator.navigate(it) }
            )
        }
    )
}