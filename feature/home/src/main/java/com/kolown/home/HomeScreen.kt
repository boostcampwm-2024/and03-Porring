package com.kolown.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.model.ImageItem
import com.porring.home.component.RandomImageList

@Composable
internal fun HomeRoute(
    padding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val mainFeedImages by viewModel.mainFeedImageItems.collectAsStateWithLifecycle()
    val onFollowClick : (Long) -> Unit = { viewModel.followUser(it) }

    HomeScreen(
        padding = padding,
        mainFeedImages = mainFeedImages,
        onFollowClick = onFollowClick
    )
}

@Composable
private fun HomeScreen(
    padding: PaddingValues = PaddingValues(),
    mainFeedImages: List<ImageItem> = emptyList(),
    onFollowClick: (Long) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { mainFeedImages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        ) {

        }
        RandomImageList(
            pagerState = pagerState,
            imageItems = mainFeedImages,
            onFollowClick = onFollowClick
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}
