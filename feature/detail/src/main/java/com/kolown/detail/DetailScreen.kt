package com.kolown.detail

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.common.component.DetailItem
import com.kolown.common.component.LoadingDetailContent
import com.kolown.designsystem.R.*
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.component.PorringTopAppBar
import com.kolown.designsystem.ui.theme.BackgroundDark
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun DetailRoute(
    isLoggedIn: Boolean,
    detailFirstItem: PostContentModel,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    popBackStack: () -> Unit,
    padding: PaddingValues = PaddingValues(),
    navigateToTheir: (String) -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel(),
    updateFollow: (String) -> Unit
) {
    var isReelsMode by remember { mutableStateOf(true) }
    val uiState = detailViewModel.uiState.collectAsStateWithLifecycle()
    val currentPage = detailViewModel.currentPage
    val followState = detailViewModel.followState.collectAsStateWithLifecycle(null)
    var isPopBackStack by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    when (val state = uiState.value) {
        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingDetailContent()
        }

        is UiState.Success -> {
            val pagingItems = state.data.collectAsLazyPagingItems()
            val pagerState = rememberPagerState(initialPage = currentPage) {
                   pagingItems.itemCount + 2
            }

            LaunchedEffect(pagingItems.itemCount) {
                if (pagerState.currentPage == 1 && currentPage != 0) pagerState.scrollToPage(currentPage)
            }

            DetailScreen(
                isLoggedIn = isLoggedIn,
                onChangeReelsMode = { isReelsMode = it },
                popBackStack = popBackStack,
                updateMainPostReaction = updateMainPostReaction,
                onSelectReaction = detailViewModel::selectReaction,
                isReelsMode = isReelsMode,
                isPopBackStack = isPopBackStack,
                firstItem = detailFirstItem,
                pagingItems = pagingItems,
                pagerState = pagerState,
                padding = padding,
                navigateToTheir = { id ->
                    coroutineScope.launch {
                        isPopBackStack = true
                        navigateToTheir(id)
                        delay(300)
                        isPopBackStack = false
                    }
                },
                updatePage = { page ->
                    detailViewModel.updatePage(page)
                },
                onFollowClick = detailViewModel::followUser,
                onUnfollowClick = detailViewModel::unFollowUser,
                followerState = followState,
                updateFollow = updateFollow
            )
        }

        is UiState.Failure -> LoadingDetailContent()
    }

    BackHandler(enabled = true) {
        isPopBackStack = true
        popBackStack()
    }
}

@Composable
private fun DetailScreen(
    isLoggedIn: Boolean = false,
    onChangeReelsMode: (Boolean) -> Unit = {},
    popBackStack: () -> Unit = {},
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    isReelsMode: Boolean = true,
    isPopBackStack:Boolean = false,
    firstItem: PostContentModel,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    padding: PaddingValues = PaddingValues(),
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    updateFollow: (String) -> Unit = {}
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
            onChangeReelsMode = onChangeReelsMode,
            updateMainPostReaction = updateMainPostReaction,
            onSelectReaction = onSelectReaction,
            pagingItems = pagingItems,
            pagerState = pagerState,
            isPopBackStack = isPopBackStack,
            firstItem = firstItem,
            navigateToTheir = navigateToTheir,
            updatePage = updatePage,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            updateFollow = updateFollow
        )

        PorringTopAppBar(
            navigationIcon = {
                if (isReelsMode) {
                    PorringIconButton(
                        icon = ImageVector.vectorResource(drawable.ic_arrow_back),
                        onClick = popBackStack,
                        contentDescription = stringResource(R.string.string_go_back),
                        color = Color.White
                    )
                }
            },
            trailingIcon = {
                if (isReelsMode.not()) {
                    PorringIconButton(
                        icon = Icons.Default.Close,
                        onClick = { onChangeReelsMode(true) },
                        contentDescription = stringResource(R.string.string_end_mode),
                        color = Color.White
                    )
                }
            }
        )
    }
}


@Composable
private fun DetailContent(
    isLoggedIn: Boolean,
    onChangeReelsMode: (Boolean) -> Unit,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    isReelsMode: Boolean,
    pagingItems: LazyPagingItems<PostContentModel>,
    isPopBackStack:Boolean = false,
    pagerState: PagerState,
    firstItem: PostContentModel,
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    updateFollow: (String) -> Unit = {}
) {

    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        userScrollEnabled = isReelsMode,
        beyondViewportPageCount = 3
    ) { page ->

        val imageItem = when (page) {
            0 -> firstItem
            pagingItems.itemCount + 1 -> null
            else -> {
                pagingItems[page - 1] ?: return@VerticalPager
            }
        }

        DetailItem(
            isLoggedIn = isLoggedIn,
            isReelsMode = isReelsMode,
            onChangeReelsMode = onChangeReelsMode,
            updateMainPostReaction = updateMainPostReaction,
            onSelectReaction = onSelectReaction,
            imageItem = imageItem,
            isPopBackStack = isPopBackStack,
            navigateToTheir = navigateToTheir,
            updatePage = {
                updatePage(pagerState.currentPage)
            },
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            updateFollow = updateFollow
        )
    }

}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DetailScreenPreview() {
//    DetailScreen()
}
