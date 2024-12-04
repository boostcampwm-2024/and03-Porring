package com.kolown.their


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

    DetailMyScreen(
        padding = padding,
        pagingItems = searchResultPost,
        popBackStack = popBackStack,
        isReelsMode = isReelsMode,
        onChangeReelsMode = { isReelsMode = it },
        pagerState = pagerState,
        onShowLoginSnackBar = onShowLoginSnackBar
    )
}

@Composable
fun DetailMyScreen(
    popBackStack: () -> Unit = {},
    isReelsMode: Boolean = true,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    padding: PaddingValues = PaddingValues(),
    updatePage: (Int) -> Unit = {},
    onChangeReelsMode: (Boolean) -> Unit,
    onShowLoginSnackBar: () -> Unit,
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
            onShowLoginSnackBar = onShowLoginSnackBar,
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
            isLoggedIn = true,
            isReelsMode = isReelsMode,
            isButtonGroupNeed = false,
            onChangeReelsMode = onChangeReelsMode,
            imageItem = imageItem,
            navigateToTheir = {},
            updatePage = {
                updatePage(pagerState.currentPage)
            },
            checkPostIsMine = checkPostIsMine,
            onShowLoginSnackBar = onShowLoginSnackBar
        )

    }

    //todo: 에러 났을 때(ex.Network Error)
}

