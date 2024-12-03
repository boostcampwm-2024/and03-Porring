package com.kolown.search


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.common.component.DetailItem
import com.kolown.designsystem.ui.theme.PrimaryContainerDark
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.search.component.DetailTopAppBar

@Composable
internal fun DetailSearchRoute(
    padding: PaddingValues = PaddingValues(),
    popBackStack: () -> Unit,
    navigateToTheir: (String) -> Unit,
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    viewModel: SearchViewModel
) {

    val searchResultPost = viewModel.resultPostList.collectAsLazyPagingItems()
    var isReelsMode by remember { mutableStateOf(true) }

    val pagerState =
        rememberPagerState(initialPage = viewModel.firstPage) { searchResultPost.itemCount }
    val followState = viewModel.followState.collectAsStateWithLifecycle(null)

    DetailSearchScreen(
        padding = padding,
        pagingItems = searchResultPost,
        popBackStack = popBackStack,
        isReelsMode = isReelsMode,
        onChangeReelsMode = { isReelsMode = it },
        navigateToTheir = navigateToTheir,
        pagerState = pagerState,
        followerState = followState,
        onFollowClick = viewModel::followUser,
        onUnfollowClick = viewModel::unFollowUser,
        onSelectReaction = viewModel::selectReaction,
        checkPostIsMine = viewModel::checkPostIsMine,
        isLoggedIn = isLoggedIn,
        onShowLoginSnackBar = onShowLoginSnackBar
    )
}

@Composable
fun DetailSearchScreen(
    popBackStack: () -> Unit = {},
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    isReelsMode: Boolean = true,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    padding: PaddingValues = PaddingValues(),
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit = {},
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    isLoggedIn: Boolean,
    onChangeReelsMode: (Boolean) -> Unit,
    onShowLoginSnackBar: () -> Unit,
    followerState: State<Pair<String, Boolean>?>,
    checkPostIsMine: (String) -> Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryContainerDark)
            .padding(padding)
    ) {

        DetailContent(
            onSelectReaction = onSelectReaction,
            isReelsMode = isReelsMode,
            onChangeReelsMode = onChangeReelsMode,
            pagingItems = pagingItems,
            pagerState = pagerState,
            navigateToTheir = navigateToTheir,
            updatePage = updatePage,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            checkPostIsMine = checkPostIsMine,
            isLoggedIn = isLoggedIn,
            onShowLoginSnackBar = onShowLoginSnackBar
        )

        DetailTopAppBar(
            isReelsMode = isReelsMode,
            onChangeReelsMode = onChangeReelsMode,
            popBackStack = popBackStack
        )
    }
}


@Composable
fun DetailContent(
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    isReelsMode: Boolean,
    onChangeReelsMode: (Boolean) -> Unit,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?> = mutableStateOf(null),
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    checkPostIsMine: (String) -> Boolean = { _ -> false }
) {

    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        userScrollEnabled = isReelsMode,
    ) { page ->

        val imageItem = pagingItems[page] ?: return@VerticalPager

        DetailItem(
            isLoggedIn = isLoggedIn,
            isReelsMode = isReelsMode,
            onChangeReelsMode = onChangeReelsMode,
            onSelectReaction = onSelectReaction,
            imageItem = imageItem,
            navigateToTheir = navigateToTheir,
            updatePage = {
                updatePage(pagerState.currentPage)
            },
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            checkPostIsMine = checkPostIsMine,
            onShowLoginSnackBar = onShowLoginSnackBar
        )

    }
}

