package com.kolown.home

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.home.component.RandomImageList
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoute(
    isLoggedIn: Boolean,
    mainItems: List<PostContentModel>,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    fetchDetailFirst: (PostContentModel) -> Unit,
    updateFollow: (String) -> Unit,
    padding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToTheir: (String) -> Unit = {},
    navigateToDetail: () -> Unit = {},
    updateMainItems: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isReactionDialogVisible by remember { mutableStateOf(false) }
    var isShowErrorScreen by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val scaleFraction = {
        if (isRefreshing) 1f
        else LinearOutSlowInEasing.transform(refreshState.distanceFraction).coerceIn(0f, 1f)
    }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        viewModel.changeLoading()
        updateMainItems()
    }

    LaunchedEffect(mainItems) {
        viewModel.updateItems(mainItems)
    }
    LaunchedEffect(uiState) {
        if (uiState is UiState.Loading) {
            isRefreshing = false
            delay(7000)
            isShowErrorScreen = true
        } else {
            isShowErrorScreen = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .pointerInput(isReactionDialogVisible) {
                if (isReactionDialogVisible) {
                    detectTapGestures { isReactionDialogVisible = false }
                }
            }
            .pullToRefresh(
                state = refreshState,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState()),
                isLoggedIn = isLoggedIn,
                isShowErrorScreen = isShowErrorScreen,
                isReactionDialogVisible = isReactionDialogVisible,
                uiState = uiState,
                padding = padding,
                onFollowClick = { id, name ->
                    viewModel.followUser(id, name)
                    updateFollow(id)
                },
                onUnfollowClick = {
                    updateFollow(it)
                    viewModel.unFollowUser(it)
                },
                navigateToTheir = navigateToTheir,
                onSelectReaction = { post, reaction ->
                    viewModel.selectReaction(post, reaction)
                    onSelectReaction(post, reaction)
                },
                fetchDetailFirst = fetchDetailFirst,
                navigateToDetail = navigateToDetail,
                updateShowErrorScreen = { isShowErrorScreen = it },
                updateIsReactionDialogVisible = { isReactionDialogVisible = it },
                changeUiStateLoading = { viewModel.changeLoading() }
            )
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        scaleX = scaleFraction()
                        scaleY = scaleFraction()
                    }
            ) {
                PullToRefreshDefaults.Indicator(state = refreshState, isRefreshing = isRefreshing)
            }
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean = false,
    isShowErrorScreen: Boolean = false,
    isReactionDialogVisible: Boolean = false,
    uiState: UiState<List<PostContentModel>> = UiState.Loading,
    onShowLoginSnackBar: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    navigateToTheir: (String) -> Unit = {},
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    fetchDetailFirst: (PostContentModel) -> Unit = {},
    navigateToDetail: () -> Unit = {},
    updateShowErrorScreen: (Boolean) -> Unit = {},
    updateIsReactionDialogVisible: (Boolean) -> Unit = {},
    changeUiStateLoading: () -> Unit = {}
) {
    when {
        isShowErrorScreen -> {
            ErrorScreen()
        }

        uiState is UiState.Failure -> {
            updateShowErrorScreen(false)
            ErrorScreen()
        }

        uiState is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp), color = Primary)
            }
        }

        uiState is UiState.Success -> {
            val images = uiState.data
            val pagerState = rememberPagerState(pageCount = { images.size })
            updateShowErrorScreen(false)
            if (images.isEmpty()) {
                changeUiStateLoading()
            } else {
                RandomImageList(
                    modifier = modifier,
                    isLoggedIn = isLoggedIn,
                    pagerState = pagerState,
                    imageItems = images,
                    isReactionDialogVisible = isReactionDialogVisible,
                    onFollowClick = onFollowClick,
                    onUnfollowClick = onUnfollowClick,
                    onSelectReaction = onSelectReaction,
                    onChangeReactionDialogVisibility = {
                        updateIsReactionDialogVisible(!isReactionDialogVisible)
                    },
                    navigateToTheir = navigateToTheir,
                    fetchDetailFirst = fetchDetailFirst,
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.Warning, contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.string_error), color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}
