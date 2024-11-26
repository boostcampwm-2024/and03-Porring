package com.kolown.porring

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
import com.kolown.porring.component.MainBottomBar
import com.kolown.porring.component.MainNavHost
import com.kolown.porring.navigation.MainMenu
import com.kolown.porring.navigation.MainNavigator
import com.kolown.porring.navigation.rememberMainNavigator
import com.kolown.porring.viewmodel.UserStateViewModel
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    userStateViewModel: UserStateViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoggedIn by userStateViewModel.loginState.collectAsStateWithLifecycle()

    MainScreenContent(
        isLoggedIn = isLoggedIn,
        updateLoginState = userStateViewModel::updateLoginState,
        navigator = navigator,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
private fun MainScreenContent(
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