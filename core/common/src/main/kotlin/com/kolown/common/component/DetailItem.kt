package com.kolown.common.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.common.R
import com.kolown.designsystem.ui.theme.Gray
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryContainerDark
import com.kolown.designsystem.ui.theme.PrimaryDark
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.designsystem.ui.theme.Surface2
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.kolown.model.SnackBarEvent


@Composable
fun DetailItem(
    isLoggedIn: Boolean,
    isReelsMode: Boolean,
    isButtonGroupNeed: Boolean = true,
    isPopBackStack: Boolean = false,
    onChangeReelsMode: (Boolean) -> Unit,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    imageItem: PostContentModel?,
    navigateToTheir: (String) -> Unit,
    updatePage: () -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?> = mutableStateOf(null),
    checkPostIsMine: (String) -> Boolean = { _ -> false },
    updateFollow: (String) -> Unit = {}
) {

    if (imageItem != null) {
        if (isReelsMode) {
            ReelsContent(
                isLoggedIn = isLoggedIn,
                isButtonGroupNeed = isButtonGroupNeed,
                updateMainPostReaction = updateMainPostReaction,
                onSelectReaction = onSelectReaction,
                imageItem = imageItem,
                onDoubleTab = {
                    onChangeReelsMode(false)
                },
                isPopBackStack = isPopBackStack,
                navigateToTheir = navigateToTheir,
                updatePage = updatePage,
                onFollowClick = onFollowClick,
                onUnfollowClick = onUnfollowClick,
                followerState = followerState,
                checkPostIsMine = checkPostIsMine,
                updateFollow = updateFollow
            )
        } else {
            ConcentrateContent(
                imageUrl = imageItem.imageUrl,
                backHandle = {
                    onChangeReelsMode(true)
                }
            )
        }
    } else {
        LoadingDetailContent()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReelsContent(
    isLoggedIn: Boolean,
    isButtonGroupNeed: Boolean,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    imageItem: PostContentModel,
    navigateToTheir: (String) -> Unit,
    updatePage: () -> Unit,
    isPopBackStack: Boolean = false,
    onDoubleTab: () -> Unit,
    checkPostIsMine: (String) -> Boolean,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    updateFollow: (String) -> Unit = {}
) {
    val isReactionVisible = remember { mutableStateOf(false) }
    val isFollowDialogVisible = remember { mutableStateOf(false) }
    val isFollowed = rememberSaveable { mutableStateOf(imageItem.isFollower) }
    val isError = remember { mutableStateOf(false) }
    val snackBarBridge = LocalSnackBarBridge.current

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

        NoRippleCoilImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f),
            imageUrl = imageItem.imageUrl,
            onClick = {
                Log.e("클릭1","")
                if (isReactionVisible.value) {
                    isReactionVisible.value = false
                }
            },
            onDoubleClick = {
                Log.e("클릭2","")
                if (!isPopBackStack && !isError.value) {
                    onDoubleTab()
                }
            },
            delay = 3000
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
                        text = tags,
                        style = MaterialTheme.typography.labelLarge,
                        color = Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!checkPostIsMine(imageItem.authorId)) {
                        Icon(
                            imageVector = if (imageItem.myReaction == null) {
                                Icons.Outlined.FavoriteBorder
                            } else {
                                Icons.Default.Favorite
                            },
                            tint = PrimaryDark,
                            contentDescription = stringResource(R.string.string_reaction_button),
                            modifier = Modifier.clickable {
                                if (isLoggedIn) isReactionVisible.value = true
                                else snackBarBridge.postSnackBarEvent(SnackBarEvent.LoginRequired())
                            }
                        )

                        if (isButtonGroupNeed) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetailButton(
                                    onClick = {
                                        if (isLoggedIn) {
                                            if (isFollowed.value) {
                                                onUnfollowClick(imageItem.authorId)
                                                updateFollow(imageItem.authorId)
                                            } else {
                                                isFollowDialogVisible.value = true
                                            }
                                        } else {
                                            snackBarBridge.postSnackBarEvent(SnackBarEvent.LoginRequired())
                                        }
                                    },
                                    id = R.drawable.ic_detail_follow,
                                    buttonText = stringResource(R.string.string_follow),
                                    contentColor = if (isFollowed.value) PrimaryContainerDark else PrimaryDark,
                                    backgroundColor = if (isFollowed.value) PrimaryDark else PrimaryContainerDark
                                )
                                DetailButton(
                                    onClick = {
                                        navigateToTheir(imageItem.authorId)
                                        updatePage()
                                    },
                                    id = R.drawable.ic_detail_gallary,
                                    buttonText = stringResource(R.string.string_gallery)
                                )
                            }
                        }
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
            updateFollow(imageItem.authorId)
        })
}


@Composable
private fun ConcentrateContent(
    imageUrl: String,
    backHandle: () -> Unit,
) {
    val view = LocalView.current

    LaunchedEffect(true) {
        requestFullScreen(view)
    }

    var scale by remember {
        mutableFloatStateOf(1f)
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
        BackHandler {
            showSystembar(view)
            backHandle()
        }
    }
}

@Composable
private fun DetailButton(
    onClick: () -> Unit,
    @DrawableRes id: Int,
    buttonText: String,
    contentColor: Color = PrimaryDark,
    backgroundColor: Color = PrimaryContainerDark,
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
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    insetController.hide(
        WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    )
}

private fun showSystembar(view: View) {
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
