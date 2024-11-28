package com.kolown.home.component

import IconFollow
import IconGallery
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kolown.designsystem.Primary
import com.kolown.home.R
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions

@Composable
internal fun RandomImageList(
    pagerState: PagerState = rememberPagerState(pageCount = { 10 }),
    imageItems: List<PostContentModel> = emptyList(),
    isReactionDialogVisible: Boolean = false,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    onChangeReactionDialogVisibility: () -> Unit = {},
    navigateToTheir: (String) -> Unit = {},
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    fetchDetailFirst: (PostContentModel) -> Unit = {},
    navigateToDetail: () -> Unit = {},
) {
    HorizontalPager(
        state = pagerState
    ) { page ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ImageCard(
                imageItem = imageItems[page],
                isReactionDialogVisible = isReactionDialogVisible,
                onFollowClick = onFollowClick,
                onUnfollowClick = onUnfollowClick,
                onChangeReactionDialogVisibility = onChangeReactionDialogVisibility,
                navigateToTheir = navigateToTheir,
                onSelectReaction = { reaction -> onSelectReaction(imageItems[page], reaction) },
                fetchDetailFirst = fetchDetailFirst,
                navigateToDetail = navigateToDetail
            )
            LottieFireWorkAnimation(
                modifier = Modifier.align(Alignment.TopEnd),
                reactions = imageItems[page].reactions,
                myReaction = imageItems[page].myReaction
            )
        }
    }
}

@Composable
private fun ImageCard(
    imageItem: PostContentModel,
    isReactionDialogVisible: Boolean,
    onFollowClick: (String, String) -> Unit,
    onUnfollowClick: (String) -> Unit,
    onSelectReaction: (Reactions) -> Unit,
    onChangeReactionDialogVisibility: () -> Unit,
    navigateToTheir: (String) -> Unit,
    fetchDetailFirst: (PostContentModel) -> Unit,
    navigateToDetail: () -> Unit,
) {
    val likedImageVector =
        if (imageItem.myReaction == null) Icons.Outlined.FavoriteBorder else Icons.Outlined.Favorite
    var isFollowDialogVisible by remember { mutableStateOf(false) }
    var isFirstRenderer by remember { mutableStateOf(true) }
    val sizeAnimation = remember { Animatable(1f) }

    LaunchedEffect(imageItem.myReaction) {
        if (!isFirstRenderer) {
            sizeAnimation.animateTo(
                targetValue = 1.4f,
                animationSpec = tween(durationMillis = 100)
            )
            sizeAnimation.animateTo(
                targetValue = 1.1f,
                animationSpec = tween(durationMillis = 100)
            )
            sizeAnimation.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(durationMillis = 100)
            )
            sizeAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 100)
            )
        } else {
            isFirstRenderer = false
        }
    }

    Column {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp)
                .fillMaxWidth()
                .aspectRatio(4f / 5f),
            contentAlignment = Alignment.Center
        ) {
            RandomImage(
                imageItem.imageUrl,
                onClickImage = {
                    fetchDetailFirst(imageItem)
                    navigateToDetail()
                }
            )
            ReactionGroup(
                modifier = Modifier.align(Alignment.TopEnd),
                reactions = imageItem.reactions
            )
            IconButtonGroup(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                imageItem = imageItem,
                navigateToTheir = { navigateToTheir(imageItem.authorId) },
                onFollowClick = {
                    if (imageItem.isFollower) {
                        onUnfollowClick(imageItem.authorId)
                    } else {
                        isFollowDialogVisible = true
                    }
                }
            )
            if (isReactionDialogVisible) {
                ReactionDialog(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    selectedReaction = imageItem.myReaction,
                    onClick = onSelectReaction,
                    onDismiss = onChangeReactionDialogVisibility
                )
            }
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            onClick = onChangeReactionDialogVisibility,
        ) {
            Icon(
                modifier = Modifier.size(30.dp * sizeAnimation.value),
                imageVector = likedImageVector,
                contentDescription = null,
                tint = Primary
            )
        }
        if (isFollowDialogVisible) {
            FollowDialog(
                onClickCancel = { isFollowDialogVisible = false },
                onClickConfirm = { name ->
                    onFollowClick(imageItem.authorId, name)
                }
            )
        }
    }
}

@Composable
private fun IconButtonGroup(
    modifier: Modifier,
    imageItem: PostContentModel,
    onFollowClick: () -> Unit,
    navigateToTheir: () -> Unit,
) {
    val whiteModifier = Modifier
        .clip(CircleShape)
        .size(48.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomIconButton(
            modifier = whiteModifier,
            imageVector = IconFollow,
            isSelected = imageItem.isFollower
        ) {
            onFollowClick()
        }
        CustomIconButton(
            modifier = whiteModifier,
            imageVector = IconGallery,
            isSelected = false,
            onClick = navigateToTheir
        )
    }
}

@Composable
private fun RandomImage(
    imageUrl: String = "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
    onClickImage: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    Box {
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            onClick = onClickImage
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onLoading = {
                    isLoading = true
                    isError = false
                },
                onSuccess = {
                    isLoading = false
                    isError = false
                },
                onError = {
                    isLoading = false
                    isError = true
                }
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }

        if (isError) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "이미지를 로드할 수 없음. 다시 시도해주세요."
                )
            }
        }
    }

}

@Composable
private fun CustomIconButton(
    modifier: Modifier,
    imageVector: ImageVector,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier.background(if (isSelected) Primary else Color.White),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = if (isSelected) Color.White else Primary
        )
    }
}

@Composable
fun LottieFireWorkAnimation(
    modifier: Modifier,
    reactions: List<Reactions>,
    myReaction: Reactions?
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fireworks))
    var isAnimationPlaying by remember { mutableStateOf(false) }
    var isFirstShow by remember { mutableStateOf(true) }

    LaunchedEffect(reactions) {
        if (reactions.isNotEmpty() && myReaction != null && !isFirstShow) {
            isAnimationPlaying = true
        }
        isFirstShow = false
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isAnimationPlaying,
        iterations = 1,
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            isAnimationPlaying = false
        }
    }

    if(isAnimationPlaying) {
        LottieAnimation(
            modifier = modifier.size(140.dp),
            composition = composition,
            progress = { progress }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRandomImageList() {
    RandomImageList()
}