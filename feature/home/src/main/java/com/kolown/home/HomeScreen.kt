package com.kolown.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.model.ImageItem
import com.kolown.model.Reactions
import com.kolown.home.component.RandomImageList

@Composable
internal fun HomeRoute(
    padding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val mainFeedImages by viewModel.mainFeedImageItems.collectAsStateWithLifecycle()
    val onFollowClick: (Long) -> Unit = { viewModel.followUser(it) }
    val onSelectReaction: (Long, Reactions) -> Unit =
        { id, reaction -> viewModel.selectReaction(id, reaction) }

    HomeScreen(
        padding = padding,
        mainFeedImages = mainFeedImages,
        onFollowClick = onFollowClick,
        onSelectReaction = onSelectReaction,
        onLoadNextPage = { viewModel.loadImageItem() }
    )
}

@Composable
private fun HomeScreen(
    padding: PaddingValues = PaddingValues(),
    mainFeedImages: List<ImageItem> = emptyList(),
    onFollowClick: (Long) -> Unit = {},
    onSelectReaction: (Long, Reactions) -> Unit = { _, _ -> },
    onLoadNextPage: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { mainFeedImages.size })
    var isReactionDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(top = 64.dp)
            .pointerInput(isReactionDialogVisible) {
                if (isReactionDialogVisible) {
                    detectTapGestures { isReactionDialogVisible = false }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RandomImageList(
            pagerState = pagerState,
            imageItems = mainFeedImages,
            isReactionDialogVisible = isReactionDialogVisible,
            onFollowClick = onFollowClick,
            onSelectReaction = onSelectReaction,
            onChangeReactionDialogVisibility = {
                isReactionDialogVisible = !isReactionDialogVisible
            },
            onLoadNextPage = onLoadNextPage
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    )
                ),
                alpha = 0.05f
            )
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}
