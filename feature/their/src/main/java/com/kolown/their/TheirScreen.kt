package com.kolown.their

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.model.PostContentModel
import com.kolown.their.component.GalleryItem
import com.kolown.their.component.PageItemFooter
import com.kolown.their.component.TheirAppBar
import kotlinx.coroutines.delay

@Composable
internal fun TheirRoute(
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
    val listState = rememberLazyStaggeredGridState()

    TheirScreen(
        popBackStack = popBackStack,
        padding = padding,
        followerName = followerName.value,
        pagingItems = pagingItems,
        listState = listState
    )
}

@Composable
private fun TheirScreen(
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    followerName: String = "",
    pagingItems: LazyPagingItems<PostContentModel>,
    listState: LazyStaggeredGridState,
) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 2

    LaunchedEffect(pagingItems) {
        listState.scrollToItem(0)
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
        StateLazyGrid(
            listState = listState,
            pagingItems = pagingItems,
            width = width
        )
    }
}

@Composable
private fun StateLazyGrid(
    listState: LazyStaggeredGridState,
    pagingItems: LazyPagingItems<PostContentModel>,
    width: Dp,
) {
    var showErrorScreen by remember { mutableStateOf(false) }

    LaunchedEffect(pagingItems.loadState.refresh) {
        if (pagingItems.loadState.refresh == LoadState.Loading) {
            delay(7000)
            showErrorScreen = true
        } else {
            showErrorScreen = false
        }
    }


    when {
        showErrorScreen -> {
            ErrorScreen()
        }

        pagingItems.loadState.refresh is LoadState.Error -> {
            Log.d("LazyVertical", "실패")
            showErrorScreen = false
            ErrorScreen()
        }

        pagingItems.loadState.refresh is LoadState.Loading -> {
            Log.d("LazyVertical", "로딩")
            showErrorScreen = false
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center),
                )
            }
        }

        pagingItems.loadState.refresh is LoadState.NotLoading -> {
            Log.d("LazyVertical", "성공 ${pagingItems.itemCount}")
            showErrorScreen = false
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
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
}

@Composable
private fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
    }
}