package com.kolown.home

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.flow.Flow

@Composable
internal fun HomeRoute(
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    mainItems: Flow<List<PostContentModel>>,
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
    var showErrorScreen by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(mainItems) {
        viewModel.updateItems(mainItems)
        isRefreshing = false
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Loading) {
            delay(5000)
            showErrorScreen = true
        } else {
            showErrorScreen = false
        }
    }

    when {
        showErrorScreen -> {
            ErrorScreen(padding)
        }

        uiState is UiState.Failure -> {
            showErrorScreen = false
            val error = (uiState as UiState.Failure).error
            Log.e("HomeRoute", "HomeRoute: $error")
            ErrorScreen(padding)
        }

        uiState is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp), color = Primary)
            }
        }

        uiState is UiState.Success -> {
            showErrorScreen = false
            val images = (uiState as UiState.Success<List<PostContentModel>>).data
            if (images.isEmpty()) {
                ErrorScreen(padding)
            } else {
                HomeScreen(
                    isLoggedIn = isLoggedIn,
                    onShowLoginSnackBar = onShowLoginSnackBar,
                    padding = padding,
                    mainFeedImages = images,
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
                    updateMainItems = updateMainItems,
                    isRefreshing = isRefreshing,
                    updateRefreshing = { isRefreshing = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    isLoggedIn: Boolean = false,
    isRefreshing: Boolean = false,
    updateRefreshing: (Boolean) -> Unit = {},
    onShowLoginSnackBar: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    mainFeedImages: List<PostContentModel> = emptyList(),
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    navigateToTheir: (String) -> Unit = {},
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    fetchDetailFirst: (PostContentModel) -> Unit = {},
    navigateToDetail: () -> Unit = {},
    updateMainItems: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { mainFeedImages.size })
    var isReactionDialogVisible by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        updateRefreshing(true)
        updateMainItems()
    }
    val scaleFraction = {
        if (isRefreshing) 1f
        else LinearOutSlowInEasing.transform(refreshState.distanceFraction).coerceIn(0f, 1f)
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
            RandomImageList(
                modifier = Modifier
                    .padding(top = 64.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState()),
                isLoggedIn = isLoggedIn,
                onShowLoginSnackBar = onShowLoginSnackBar,
                pagerState = pagerState,
                imageItems = mainFeedImages,
                isReactionDialogVisible = isReactionDialogVisible,
                onFollowClick = onFollowClick,
                onUnfollowClick = onUnfollowClick,
                onSelectReaction = onSelectReaction,
                onChangeReactionDialogVisibility = {
                    isReactionDialogVisible = !isReactionDialogVisible
                },
                navigateToTheir = navigateToTheir,
                fetchDetailFirst = fetchDetailFirst,
                navigateToDetail = navigateToDetail
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
private fun ErrorScreen(padding: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(top = 64.dp)
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
                text = "오류가 발생하였습니다!", color = Color.Red
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, Color.Black
                        )
                    ), alpha = 0.05f
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}
