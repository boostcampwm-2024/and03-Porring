package com.kolown.my

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
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kolown.common.component.RestrictedLoginContent
import com.kolown.designsystem.R
import com.kolown.designsystem.component.PorringCenterAlignTopAppBar
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.model.PostContentModel
import com.kolown.my.component.GalleryItem
import com.kolown.my.component.PageItemFooter
import kotlinx.coroutines.delay

@Composable
internal fun MyRoute(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    navigateToSetting: () -> Unit,
    padding: PaddingValues = PaddingValues(),
) {
    if (isLoggedIn) {
        MyScreen(
            navigateToSetting = navigateToSetting,
            padding = padding
        )
    } else {
        RestrictedLoginContent(navigateToLogin)
    }
}

@Composable
private fun MyScreen(
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
        PorringCenterAlignTopAppBar(
            title = "My Gallery",
            trailingIcon = {
                PorringIconButton(
                    icon = Icons.Default.Settings,
                    onClick = navigateToSetting,
                    contentDescription = "설정"
                )
            }
        )
        StateLazyGrid(
            listState = listState,
            pagingItems = pagingItems,
            width = width,
            deletePost = viewModel::deletePost
        )
    }
}

@Composable
fun StateLazyGrid(
    listState: LazyStaggeredGridState,
    pagingItems: LazyPagingItems<PostContentModel>,
    width: Dp,
    deletePost: (String) -> Unit,
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
            showErrorScreen = false
            ErrorScreen()
        }

        pagingItems.loadState.refresh is LoadState.Loading -> {
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
            showErrorScreen = false
            if (pagingItems.itemCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(100.dp),
                            model = R.drawable.ic_question_mark,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Text(
                            text = "게시물이 존재하지 않습니다!",
                            color = Primary
                        )
                    }
                }
            } else {
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
                                GalleryItem(it, width) {
                                    deletePost(it.postId)
                                }
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

@Preview(showBackground = true)
@Composable
private fun PreviewMyScreen() {
    MyScreen()
}
