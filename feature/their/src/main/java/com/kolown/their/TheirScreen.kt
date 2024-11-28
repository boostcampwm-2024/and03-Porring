package com.kolown.their

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.model.PostContentModel
import com.kolown.their.component.GalleryItem
import com.kolown.their.component.PageItemFooter
import com.kolown.their.component.RestrictedLoginContent
import com.kolown.their.component.TheirAppBar

@Composable
internal fun TheirRoute(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    popBackStack: () -> Unit,
    padding: PaddingValues = PaddingValues(),
    followerId: String,
    viewModel: TheirViewModel = hiltViewModel(),
) {
    LaunchedEffect(followerId) {
        viewModel.setFollowerName(followerId)
    }

    val followerName = viewModel.followerName.collectAsStateWithLifecycle()
    val pagingItems = viewModel.galleryFlow.collectAsLazyPagingItems()
    val pagerState = rememberLazyStaggeredGridState()

    if (isLoggedIn) {
        TheirScreen(
            popBackStack = popBackStack,
            padding = padding,
            followerName = followerName.value,
            pagingItems = pagingItems,
            pagerState = pagerState
        )
    } else {
        RestrictedLoginContent(navigateToLogin)
    }
}

@Composable
fun TheirScreen(
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    followerName: String = "",
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: LazyStaggeredGridState,
) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 2

    LaunchedEffect(pagingItems) {
        pagerState.scrollToItem(0)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        TheirAppBar(
            popBackStack = popBackStack,
            followerName = followerName
        )
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            content = {
                items(pagingItems.itemCount) { index ->
                    pagingItems[index]?.let {
                        GalleryItem(it, width)
                    }
                }

                if (pagingItems.loadState.append !is LoadState.NotLoading) {
                    item(key = "", span = StaggeredGridItemSpan.FullLine) {
                        PageItemFooter(loadState = pagingItems.loadState.append) {
                            pagingItems.retry()
                        }
                    }
                }
            }
        )
    }

}