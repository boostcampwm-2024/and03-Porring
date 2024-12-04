package com.kolown.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kolown.common.component.showSnackBarWithData
import com.kolown.designsystem.ui.theme.PrimaryDark
import com.kolown.designsystem.ui.theme.SnackBarContainer
import com.kolown.main.component.MainBottomBar
import com.kolown.main.component.MainNavHost
import com.kolown.main.navigation.MainMenu
import com.kolown.main.navigation.MainNavigator
import com.kolown.main.navigation.rememberMainNavigator
import com.kolown.model.InitUiState
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.SnackBarData
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val mainItems by mainViewModel.mainItems.collectAsStateWithLifecycle()
    val detailFirstItem by mainViewModel.detailFirstItem.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoggedIn by mainViewModel.loginState.collectAsStateWithLifecycle()

    val uploadState by mainViewModel.upLoadUiState.collectAsStateWithLifecycle()
    val uploadModel by mainViewModel.uploadModel.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        mainViewModel.snackBarFlow.collect {
            when (it) {
                is SnackBarData.LoginRequired -> {
                    snackBarHostState.showSnackBarWithData(it).let { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            if (!isLoggedIn) navigator.navigateToLogin()
                        }
                    }

                }

                is SnackBarData.Message -> {
                    snackBarHostState.showSnackBarWithData(it)
                }
            }

        }
    }

    val onShowLoginSnackBar: () -> Unit = {
        lifecycleScope.launch {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.string_need_login),
                actionLabel = context.getString(R.string.string_login),
                duration = SnackbarDuration.Short
            ).let { result ->
                if (result == SnackbarResult.ActionPerformed) {
                    navigator.navigateToLogin()
                }
            }
        }
    }

    LaunchedEffect(uploadState) {
        if (uploadState != InitUiState.Init) {
            snackBarHostState.currentSnackbarData?.dismiss()
        }

        when (uploadState) {
            InitUiState.Init -> {}

            is InitUiState.Failure -> {
                coroutineScope.launch {
                    val result = snackBarHostState.showSnackbar(
                        message = context.getString(R.string.string_upload_fail),
                        actionLabel = context.getString(R.string.string_move),
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        navigator.navigateToUpload("", uploadModel)
                    }
                }
            }

            InitUiState.Loading -> {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        message = context.getString(R.string.string_uploading),
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }

            is InitUiState.Success -> {
                coroutineScope.launch {
                    val result = snackBarHostState.showSnackbar(
                        message = context.getString(R.string.string_upload_success),
                        actionLabel = context.getString(R.string.string_move),
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        navigator.navigate(MainMenu.MY)
                    }
                    mainViewModel.resetUploadState()
                }
            }
        }
    }

    MainScreenContent(
        navigator = navigator,
        mainItems = mainItems,
        detailFirstItem = detailFirstItem,
        isLoggedIn = isLoggedIn,

        snackBarHostState = snackBarHostState,
        onSelectReaction = mainViewModel::selectReaction,
        fetchDetailFirst = mainViewModel::fetchDetailFirst,
        updateFollow = mainViewModel::updateFollow,
        updateLoginState = mainViewModel::updateLoginState,
        uploadPost = mainViewModel::uploadPost,
        updateMainItems = mainViewModel::refreshImageItem
    )
}

//@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    mainItems: Flow<List<PostContentModel>>,
    isLoggedIn: Boolean,
    detailFirstItem: PostContentModel,
    updateLoginState: () -> Unit,
    updateMainItems: () -> Unit,
    updateFollow: (String) -> Unit,
    uploadPost: (String, String, List<String>) -> Unit,
    fetchDetailFirst: (PostContentModel) -> Unit,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { CustomSnackBar(snackBarHostState) },
        content = { padding ->
            MainNavHost(
                mainItems = mainItems,
                onSelectReaction = onSelectReaction,
                detailFirstItem = detailFirstItem,
                fetchDetailFirst = fetchDetailFirst,
                isLoggedIn = isLoggedIn,
                navigator = navigator,
                padding = padding,
                updateFollow = updateFollow,
                updateLoginState = updateLoginState,
                uploadPost = uploadPost,
                updateMainItems = updateMainItems
            )
        },
        bottomBar = {
            MainBottomBar(
                isLoggedIn = isLoggedIn,

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

@Composable
private fun CustomSnackBar(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    SnackbarHost(hostState = snackBarHostState) { snackBarData ->
        Snackbar(
            snackbarData = snackBarData,
            containerColor = SnackBarContainer,
            contentColor = PrimaryDark,
            actionContentColor = Color.Green
        )
    }
}

@Preview
@Composable
private fun CustomSnackBarPreview() {
    CustomSnackBar()
}
