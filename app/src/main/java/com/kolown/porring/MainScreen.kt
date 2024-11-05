package com.kolown.porring

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kolown.porring.component.MainBottomBar
import com.kolown.porring.component.MainNavHost
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    MainScreenContent(
        navigator = navigator,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
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
        },
    )
}