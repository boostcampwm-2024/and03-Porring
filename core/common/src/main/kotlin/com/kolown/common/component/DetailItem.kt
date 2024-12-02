package com.kolown.common.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.common.R
import com.kolown.designsystem.Gray
import com.kolown.designsystem.PrimaryDark
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions


@Composable
fun DetailItem(
    isLoggedIn: Boolean,
    isReelsMode: Boolean,
    onShowLoginSnackBar: () -> Unit,
    onChangeReelsMode: (Boolean) -> Unit,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    imageItem: PostContentModel,
    onDoubleTab: (Boolean) -> Unit,
    navigateToTheir: (String) -> Unit,
    updatePage: () -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    checkPostIsMine: (String) -> Boolean = { _ -> false }
) {
    val view = LocalView.current

    if (isReelsMode) {
        onDoubleTab(true)
        showSystembar(view = view)
    }

    if (isReelsMode) {
        ReelsContent(
            isLoggedIn = isLoggedIn,
            onShowLoginSnackBar = onShowLoginSnackBar,
            updateMainPostReaction = updateMainPostReaction,
            onSelectReaction = onSelectReaction,
            imageItem = imageItem,
            onDoubleTab = {
                onChangeReelsMode(false)
                requestFullScreen(view)
                onDoubleTab(false)
            },
            navigateToTheir = navigateToTheir,
            updatePage = updatePage,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            checkPostIsMine = checkPostIsMine
        )
    } else {
        ConcentrateContent(
            imageUrl = imageItem.imageUrl
        )
        BackHandler(enabled = true) {
            onChangeReelsMode(true)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReelsContent(
    isLoggedIn: Boolean,
    onShowLoginSnackBar: () -> Unit,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    imageItem: PostContentModel,
    navigateToTheir: (String) -> Unit,
    updatePage: () -> Unit,
    onDoubleTab: () -> Unit,
    checkPostIsMine: (String) -> Boolean,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isReactionVisible = remember { mutableStateOf(false) }
    val isFollowDialogVisible = remember { mutableStateOf(false) }
    val isFollowed = rememberSaveable { mutableStateOf(imageItem.isFollower) }


    LaunchedEffect(followerState.value) {
        followerState.value?.let { pair ->
            if (pair.first == imageItem.authorId) isFollowed.value = pair.second
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .pointerInput(isReactionVisible.value) {
                if (isReactionVisible.value) {
                    detectTapGestures { isReactionVisible.value = false }
                }
            },
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        val tags = imageItem.tags.joinToString(", ") { "#$it" }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageItem.imageUrl)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .combinedClickable(indication = null,
                    interactionSource = interactionSource,
                    onClick = {
                        if (isReactionVisible.value) {
                            isReactionVisible.value = false
                        }
                    },
                    onDoubleClick = { onDoubleTab() }),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = imageItem.description,
                            modifier = Modifier.weight(1f),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        ReactionGroup(
                            modifier = Modifier
                                .wrapContentWidth()
                                .fillMaxHeight(),
                            reactions = imageItem.reactions
                        )
                    }

                    Text(
                        text = tags, style = MaterialTheme.typography.labelLarge, color = Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = if (imageItem.myReaction == null) Icons.Outlined.FavoriteBorder else Icons.Default.Favorite,
                        tint = PrimaryDark,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            isReactionVisible.value = true
                        })

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!checkPostIsMine(imageItem.authorId)) {
                            DetailButton(
                                onClick = {
                                    if (isLoggedIn) {
                                        if (isFollowed.value) {
                                            onUnfollowClick(imageItem.authorId)
                                        } else {
                                            isFollowDialogVisible.value = true
                                        }
                                    } else {
                                        onShowLoginSnackBar()
                                    }
                                },
                                id = R.drawable.ic_detail_follow,
                                buttonText = "팔로우",
                                contentColor = if (isFollowed.value) Color(0xFF151D37) else Color(
                                    0xFF00BBFF
                                ),
                                backgroundColor = if (isFollowed.value) Color(0xFF00BBFF) else Color(
                                    0xFF151D37
                                )
                            )
                        }
                        DetailButton(
                            onClick = {
                                navigateToTheir(imageItem.authorId)
                                updatePage()
                            },
                            id = R.drawable.ic_detail_gallary,
                            buttonText = "갤러리"
                        )
                    }
                }
            }

            if (isReactionVisible.value) {
                ReactionDialog(imageItem = imageItem,
                    modifier = Modifier.padding(16.dp),
                    updateMainPostReaction = updateMainPostReaction,
                    selectedReaction = onSelectReaction,
                    onDismiss = { isReactionVisible.value = false })
            }
        }
    }

    if (isFollowDialogVisible.value)
        FollowDialog(onClickCancel = {
            isFollowDialogVisible.value = false
        }, onClickConfirm = { name ->
            onFollowClick(imageItem.authorId, name)
        })
}


@Composable
private fun ConcentrateContent(
    imageUrl: String,
) {
    var scale by remember {
        mutableStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            //이동할 수 있는 최대 거리
            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY)
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .align(Alignment.Center)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
    }
}


@Composable
private fun DetailButton(
    onClick: () -> Unit,
    @DrawableRes id: Int,
    buttonText: String,
    contentColor: Color = Color(0xFF00BBFF),
    backgroundColor: Color = Color(0xFF151D37)
) {
    Button(
        onClick = onClick,
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Icon(
            painter = painterResource(id = id), contentDescription = null, tint = contentColor
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = buttonText, color = contentColor)
    }
}

private fun requestFullScreen(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    insetController.hide(
        WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    )
}

private fun showSystembar(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    insetController.show(
        WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    )
}


private fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
