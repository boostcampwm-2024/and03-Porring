package com.kolown.follower

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.common.component.RestrictedLoginContent
import com.kolown.designsystem.component.PorringCenterAlignTopAppBar
import com.kolown.follower.component.PageItemFooter
import com.kolown.model.FollowerThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FollowerRoute(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    navigateToTheir: (String) -> Unit,
    padding: PaddingValues = PaddingValues(),
    viewModel: FollowerViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.followerItems.collectAsLazyPagingItems()
    val pagerState = rememberLazyListState()
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        pagingItems.refresh()
    }
    val scaleFraction = {
        if (isRefreshing) 1f
        else LinearOutSlowInEasing.transform(refreshState.distanceFraction).coerceIn(0f, 1f)
    }
    LaunchedEffect(pagingItems.loadState) {
        isRefreshing = false
    }

    if (isLoggedIn) {
        LaunchedEffect(Unit) {
            viewModel.resetGalleryFlow()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullToRefresh(
                    state = refreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh
                )
        ) {
            when (pagingItems.loadState.refresh) {
                is LoadState.Loading -> {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }
                }

                is LoadState.NotLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                    ) {
                        PorringCenterAlignTopAppBar(title = "Following")
                        if (pagingItems.itemCount != 0) {
                            FollowerScreen(
                                items = pagingItems,
                                pagerState = pagerState,
                                navigateToTheir = navigateToTheir
                            )
                        } else NoFollowerScreen()
                    }
                }

                is LoadState.Error -> {
                    ErrorScreen()
                }
            }
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
    } else {
        RestrictedLoginContent(
            navigateToLogin = navigateToLogin,
        )
    }
}

@Composable
private fun FollowerScreen(
    items: LazyPagingItems<FollowerThumbnail>,
    pagerState: LazyListState,
    navigateToTheir: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp),
        state = pagerState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.itemCount) { index ->
            items[index]?.let {
                FollowContent(
                    followerName = it.followerName,
                    followAlbums = it.posts,
                    navigateToTheir = { navigateToTheir(it.id) }
                )
            }
        }
        if (items.loadState.append !is LoadState.NotLoading) {
            item(key = "") {
                PageItemFooter(loadState = items.loadState.append) {
                    items.retry()
                }
            }
        }
    }
}

@Composable
internal fun FollowContent(
    followerName: String,
    followAlbums: List<String>,
    navigateToTheir: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                navigateToTheir()
            },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = followerName,
                color = Color(0xFF598AFF),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Edit", color = Color.Black, style = MaterialTheme.typography.labelMedium)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp
            )
        ) {
            followAlbums.forEach { imageUrl ->
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                            .crossfade(true).build(),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "follower's image",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

@Composable
fun NoFollowerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("팔로잉 하고 있는 사람이 없습니다.", color = Color.Gray)
    }
}

@Composable
private fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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