package com.kolown.follower

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.follower.component.FollowContent
import com.kolown.follower.component.PageItemFooter
import com.kolown.follower.component.RestrictedLoginContent
import com.kolown.model.FollowerThumbnail
import com.kolown.model.UiState
import kotlinx.coroutines.flow.Flow

@Composable
internal fun FollowerRoute(
    isLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    padding: PaddingValues = PaddingValues(),
    viewModel: FollowerViewModel = hiltViewModel()
) {
    if (isLoggedIn) {
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val pagerState = rememberLazyListState()
        when (uiState.value) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                }
            }

            is UiState.Success -> {
                val items =
                    (uiState.value as UiState.Success<Flow<PagingData<FollowerThumbnail>>>).data.collectAsLazyPagingItems()
                FollowerScreen(
                    items = items,
                    pagerState = pagerState,
                    padding = padding
                )
            }

            is UiState.Failure -> {}
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
    padding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color.White),
        contentPadding = PaddingValues(16.dp),
        state = pagerState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.itemCount) { index ->
            items[index]?.let {
                FollowContent(
                    followerName = it.followerName,
                    followAlbums = it.posts.take(3)
                        .map { post -> post.imageUrl }
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
    followAlbums: List<String>
) {
    val scrollableState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
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
            modifier = Modifier.horizontalScroll(state = scrollableState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            followAlbums.forEach { imageUrl ->
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "follower's image",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}
