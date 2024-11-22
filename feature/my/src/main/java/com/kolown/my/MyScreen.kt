package com.kolown.my

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.my.component.GalleryItem
import com.kolown.my.component.MyAppBar
import com.kolown.my.component.PageItemFooter

@Composable
internal fun MyRoute(
    navigateToSetting: () -> Unit,
    padding: PaddingValues = PaddingValues(),
) {
    MyScreen(
        navigateToSetting = navigateToSetting,
        padding = padding
    )
}

@Composable
fun MyScreen(
    navigateToSetting: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
    viewModel: MyViewModel = hiltViewModel(),
) {

    val width = LocalConfiguration.current.screenWidthDp.dp / 2
    val pagingItems = viewModel.galleryFlow.collectAsLazyPagingItems()
    val listState = rememberLazyStaggeredGridState()

    LaunchedEffect(pagingItems) {
        listState.scrollToItem(0)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        MyAppBar(
            onSettingClicked = navigateToSetting
        )
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

@Preview(showBackground = true)
@Composable
private fun PreviewMyScreen() {
    MyScreen()
}
