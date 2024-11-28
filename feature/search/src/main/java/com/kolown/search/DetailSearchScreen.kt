package com.kolown.search

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.search.component.DetailTopAppBar
import com.kolown.search.component.FollowDialog
import com.kolown.search.component.ReactionDialog
import com.kolown.search.component.ReactionGroup


@Composable
internal fun DetailSearchRoute(
    padding: PaddingValues = PaddingValues(),
    popBackStack: () -> Unit,
    navigateToTheir: (String) -> Unit,
    viewModel: SearchViewModel
) {

    val searchResultPost = viewModel.resultPostList.collectAsLazyPagingItems()

    val pagerState =
        rememberPagerState(initialPage = viewModel.firstPage) { searchResultPost.itemCount }
    val followState = viewModel.followState.collectAsStateWithLifecycle(null)

    DetailSearchScreen(
        padding = padding,
        pagingItems = searchResultPost,
        popBackStack = popBackStack,
        navigateToTheir = navigateToTheir,
        pagerState = pagerState,
        followerState = followState,
        onFollowClick = viewModel::followUser,
        onUnfollowClick = viewModel::unFollowUser,
        updatePage = { page ->
            viewModel.updatePage(page)
        },
        checkPostIsMine = viewModel::checkPostIsMine
    )
}

@Composable
fun DetailSearchScreen(
    popBackStack: () -> Unit = {},
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    viewModeChange: (Boolean) -> Unit = {},
    isReelsMode: Boolean = true,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    padding: PaddingValues = PaddingValues(),
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit = {},
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    checkPostIsMine: (String) -> Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff151D37))
            .padding(padding)
    ) {

        DetailContent(
            onSelectReaction = onSelectReaction,
            viewModeChange = viewModeChange,
            isReelsMode = isReelsMode,
            pagingItems = pagingItems,
            pagerState = pagerState,
            navigateToTheir = navigateToTheir,
            updatePage = updatePage,
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            checkPostIsMine = checkPostIsMine
        )


        if (isReelsMode) {
            DetailTopAppBar(popBackStack)
        }
    }
}


@Composable
fun DetailContent(
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    viewModeChange: (Boolean) -> Unit,
    isReelsMode: Boolean,
    pagingItems: LazyPagingItems<PostContentModel>,
    pagerState: PagerState,
    navigateToTheir: (String) -> Unit,
    updatePage: (Int) -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    checkPostIsMine: (String) -> Boolean
) {

    VerticalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        userScrollEnabled = isReelsMode,
    ) { page ->

        val imageItem = pagingItems[page] ?: return@VerticalPager

        DetailItem(
            onSelectReaction = onSelectReaction,
            imageItem = imageItem,
            onDoubleTab = viewModeChange,
            navigateToTheir = navigateToTheir,
            updatePage = {
                updatePage(pagerState.currentPage)
            },
            onFollowClick = onFollowClick,
            onUnfollowClick = onUnfollowClick,
            followerState = followerState,
            checkPostIsMine = checkPostIsMine
        )

    }

    //todo: 에러 났을 때(ex.Network Error)
}


@Composable
fun DetailItem(
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    imageItem: PostContentModel,
    onDoubleTab: (Boolean) -> Unit,
    navigateToTheir: (String) -> Unit,
    updatePage: () -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    checkPostIsMine: (String) -> Boolean
) {
    val view = LocalView.current
    val isConcentrateMode = remember {
        mutableStateOf(false)
    }

    if (!isConcentrateMode.value) {
        ReelsContent(
            updateMainPostReaction = updateMainPostReaction,
            onSelectReaction = onSelectReaction,
            imageItem = imageItem,
            onDoubleTab = {
                isConcentrateMode.value = true
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
            if (isConcentrateMode.value) {
                isConcentrateMode.value = false
                onDoubleTab(true)
                showSystembar(view = view)
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelsContent(
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    onSelectReaction: (PostContentModel, Reactions) -> Unit = { _, _ -> },
    imageItem: PostContentModel,
    navigateToTheir: (String) -> Unit,
    updatePage: () -> Unit,
    onDoubleTab: () -> Unit,
    onFollowClick: (String, String) -> Unit = { _, _ -> },
    onUnfollowClick: (String) -> Unit = {},
    followerState: State<Pair<String, Boolean>?>,
    checkPostIsMine: (String) -> Boolean
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageItem.imageUrl)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .combinedClickable(indication = null,
                    interactionSource = interactionSource,
                    onClick = {},
                    onDoubleClick = { onDoubleTab() }),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
        Box {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = imageItem.description,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
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
                val tags = imageItem.tags.joinToString(", ") { "#$it" }
                Text(
                    text = tags,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF8D8D8D)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (imageItem.myReaction == null) Icons.Outlined.FavoriteBorder else Icons.Default.Favorite,
                        tint = Color(0xFF00BBFF),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            isReactionVisible.value = true
                        })
                    Row {
                        if (checkPostIsMine(imageItem.authorId)) {
                            DetailButton(
                                onClick = {
                                    if (isFollowed.value) {
                                        onUnfollowClick(imageItem.authorId)
                                    } else {
                                        isFollowDialogVisible.value = true
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
                            Spacer(modifier = Modifier.width(10.dp))
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
                ReactionDialog(
                    imageItem = imageItem,
                    modifier = Modifier
                        .padding(16.dp),
                    updateMainPostReaction = updateMainPostReaction,
                    selectedReaction = onSelectReaction,
                    onDismiss = { isReactionVisible.value = false }
                )
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
fun ConcentrateContent(
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
fun DetailButton(
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

fun requestFullScreen(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    insetController.hide(
        WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    )
}

fun showSystembar(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    insetController.show(
        WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    )
}


fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}