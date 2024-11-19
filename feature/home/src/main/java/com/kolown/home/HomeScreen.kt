package com.kolown.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.kolown.home.component.RandomImageList
import com.kolown.model.ImageItem
import com.kolown.model.Reactions
import com.kolown.model.UiState

@Composable
internal fun HomeRoute(
    padding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel(),
    onClickImage: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is UiState.Failure -> {
            val error = (uiState as UiState.Failure).error
            Log.d("HomeRoute", "HomeRoute: $error")
            ErrorScreen(padding)
        }

        UiState.Loading -> {
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
            val images = (uiState as UiState.Success<List<ImageItem>>).data
            HomeScreen(
                padding = padding,
                mainFeedImages = images,
                onFollowClick = viewModel::followUser,
                onSelectReaction = viewModel::selectReaction,
                onClickImage = onClickImage
            )
        }
    }


}

@Composable
private fun HomeScreen(
    padding: PaddingValues = PaddingValues(),
    mainFeedImages: List<ImageItem> = emptyList(),
    onFollowClick: (Long) -> Unit = {},
    onSelectReaction: (Long, Reactions) -> Unit = { _, _ -> },
    onClickImage: () -> Unit = {}
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
            onClickImage = onClickImage
        )
        Spacer(
            modifier = Modifier
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

@Composable
private fun ErrorScreen(padding: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(top = 64.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.Warning,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "오류가 발생하였습니다!",
                color = Color.Red
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
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
