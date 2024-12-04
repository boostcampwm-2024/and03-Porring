package com.kolown.their


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.common.component.DetailItem
import com.kolown.common.component.DetailTopAppBar
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions

@Composable
internal fun DetailTheirRoute(
    padding: PaddingValues = PaddingValues(),
    popBackStack: () -> Unit,
    onShowLoginSnackBar: () -> Unit,
    viewModel: TheirViewModel
) {
    val searchResultPost = viewModel.galleryFlow.collectAsLazyPagingItems()
    var isReelsMode by remember { mutableStateOf(true) }

    val pagerState =
        rememberPagerState(initialPage = viewModel.firstPage) { searchResultPost.itemCount }
    var blockDoubleTab by remember { mutableStateOf(false) }

    DetailMyScreen(
        padding = padding,
        pagingItems = searchResultPost,
        popBackStack = popBackStack,
        isReelsMode = isReelsMode,
        blockDoubleTab = blockDoubleTab,
        onChangeReelsMode = { isReelsMode = it },
        pagerState = pagerState,
        onShowLoginSnackBar = onShowLoginSnackBar,
        selectReaction = viewModel::selectReaction,
    )

    BackHandler(enabled = true) {
        blockDoubleTab = true
        popBackStack()
    }
}

@Composable
fun DetailMyScreen(
    isReelsMode: Boolean = true,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    padding: PaddingValues = PaddingValues(),
    updatePage: (Int) -> Unit = {},
    blockDoubleTab:Boolean = false,
    onChangeReelsMode: (Boolean) -> Unit,
    onShowLoginSnackBar: () -> Unit,
    popBackStack: () -> Unit = {},
    selectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff151D37))
            .padding(padding)
    ) {

        DetailContent(
            isReelsMode = isReelsMode,
            onChangeReelsMode = onChangeReelsMode,
            pagingItems = pagingItems,
            pagerState = pagerState,
            updatePage = updatePage,
            blockDoubleTab = blockDoubleTab,
            onShowLoginSnackBar = onShowLoginSnackBar,
            selectReaction = selectReaction
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
    isReelsMode: Boolean,
    onChangeReelsMode: (Boolean) -> Unit,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    updatePage: (Int) -> Unit,
    blockDoubleTab:Boolean = false,
    onShowLoginSnackBar: () -> Unit,
    checkPostIsMine: (String) -> Boolean = { _ -> true },
    selectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> }
) {

    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        userScrollEnabled = isReelsMode,
    ) { page ->

        val imageItem = pagingItems[page] ?: return@VerticalPager

        DetailItem(
            isLoggedIn = true,
            isReelsMode = isReelsMode,
            isButtonGroupNeed = false,
            onChangeReelsMode = onChangeReelsMode,
            onSelectReaction = selectReaction,
            imageItem = imageItem,
            isPopBackStack = blockDoubleTab,
            navigateToTheir = {},
            updatePage = {
                updatePage(pagerState.currentPage)
            },
            checkPostIsMine = checkPostIsMine,
        )

    }

    //todo: 에러 났을 때(ex.Network Error)
}

