package com.kolown.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.common.component.DetailItem
import com.kolown.designsystem.ui.theme.BackgroundDark
import com.kolown.detail.component.DetailTopAppBar
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun DetailRoute(
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    detailFirstItem: PostContentModel,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    popBackStack: () -> Unit,
    padding: PaddingValues = PaddingValues(),
    navigateToTheir: (String) -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    var isReelsMode by remember { mutableStateOf(true) }
    val uiState = detailViewModel.uiState.collectAsStateWithLifecycle()
    val currentPage = detailViewModel.currentPage
    val followState = detailViewModel.followState.collectAsStateWithLifecycle(null)
    val state = uiState.value
    when (state) {
        is UiState.Idle -> {}
        is UiState.Loading -> LoadingDetailScreen()
        is UiState.Success -> {
            val pagingItems = state.data.collectAsLazyPagingItems()
            val pagerState =
                rememberPagerState(initialPage = currentPage) { pagingItems.itemCount + 1 }
            LaunchedEffect(pagingItems.itemCount) {
                if (pagerState.currentPage == 0) pagerState.scrollToPage(currentPage)
            }

            DetailScreen(
                isLoggedIn = isLoggedIn,
                onShowLoginSnackBar = onShowLoginSnackBar,
                onChangeReelsMode = { isReelsMode = it },
                popBackStack = popBackStack,
                updateMainPostReaction = updateMainPostReaction,
                onSelectReaction = detailViewModel::selectReaction,
                viewModeChange = { isReelsMode = it },
                isReelsMode = isReelsMode,
                firstItem = detailFirstItem,
                pagingItems = pagingItems,
                pagerState = pagerState,
                padding = padding,
                navigateToTheir = navigateToTheir,
                updatePage = { page ->
                    detailViewModel.updatePage(page)
                },
                onFollowClick = detailViewModel::followUser,
                onUnfollowClick = detailViewModel::unFollowUser,
                followerState = followState
            )
        }

        is UiState.Failure -> {}
    }
}

@Composable
private fun DetailScreen(
    isLoggedIn: Boolean = false,
    onShowLoginSnackBar: () -> Unit = {},
    onChangeReelsMode: (Boolean) -> Unit = {},
    popBackStack: () -> Unit = {},
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    viewModeChange: (Boolean) -> Unit = {},
    isReelsMode: Boolean = true,
    firstItem: PostContentModel,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    padding: PaddingValues = PaddingValues(),
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(padding)
    ) {
        DetailContent(
            isLoggedIn = isLoggedIn,
            isReelsMode = isReelsMode,
            onShowLoginSnackBar = onShowLoginSnackBar,
            onChangeReelsMode = onChangeReelsMode,
            updateMainPostReaction = updateMainPostReaction,
            onSelectReaction = onSelectReaction,
            viewModeChange = viewModeChange,
            pagingItems = pagingItems,
            pagerState = pagerState,
            firstItem = firstItem,
            navigateToTheir = navigateToTheir,
            updatePage = updatePage,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState
        )

        DetailTopAppBar(
            isReelsMode = isReelsMode,
            onChangeReelsMode = onChangeReelsMode,
            popBackStack = popBackStack
        )
    }
}


@Composable
private fun DetailContent(
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    onChangeReelsMode: (Boolean) -> Unit,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    viewModeChange: (Boolean) -> Unit,
    isReelsMode: Boolean,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    firstItem: PostContentModel,
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
) {

    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        userScrollEnabled = isReelsMode,
    ) { page ->
        // Our page content
        // 정상 상태일 때
        val imageItem = if (page == 0) firstItem else pagingItems[page - 1] ?: return@VerticalPager

        DetailItem(
            isLoggedIn = isLoggedIn,
            isReelsMode = isReelsMode,
            onShowLoginSnackBar = onShowLoginSnackBar,
            onChangeReelsMode = onChangeReelsMode,
            updateMainPostReaction = updateMainPostReaction,
            onSelectReaction = onSelectReaction,
            imageItem = imageItem,
            onDoubleTab = viewModeChange,
            navigateToTheir = navigateToTheir,
            updatePage = {
                updatePage(pagerState.currentPage)
            },
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState
        )
    }

    //todo: 에러 났을 때(ex.Network Error)
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DetailScreenPreview() {
//    DetailScreen()
}