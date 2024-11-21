package com.kolown.detail

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.detail.component.FollowDialog
import com.kolown.detail.component.ReactionDialog
import com.kolown.model.ImageItem
import com.kolown.model.PostContentModel
import com.kolown.model.UiState
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun DetailRoute(
    padding: PaddingValues = PaddingValues(),
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val uiState = detailViewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState.value
    when (state) {
        is UiState.Loading -> LoadingDetailScreen()
        is UiState.Success -> {
            val pagingItems = state.data.collectAsLazyPagingItems()
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = {
                    pagingItems.itemCount + 1
                }
            )
            DetailPager(
                firstItem = detailViewModel.getPagingItem(),
                padding = padding,
                items = pagingItems,
                pagerState = pagerState
            )
        }

        is UiState.Failure -> {}
    }
}


@Composable
fun DetailPager(
    firstItem: PostContentModel,
    padding: PaddingValues,
    items: LazyPagingItems<PostContentModel>,
    pagerState: PagerState
) {
    val isScrollEnabled = remember { mutableStateOf(true) }
    Log.e("페이지 카운트", pagerState.currentPage.toString())
    VerticalPager(
        state = pagerState,
        userScrollEnabled = isScrollEnabled.value,
        contentPadding = padding
    ) { page ->
        // Our page content
        // 정상 상태일 때
        if (page == 0) {
            DetailScreen(
                imageItem = firstItem,
                page = page,
                onDoubleTab = {
                    isScrollEnabled.value = it
                }
            )
        } else {
            items[page-1]?.let {
                DetailScreen(
                    imageItem = it,
                    page = page,
                    onDoubleTab = {
                        isScrollEnabled.value = it
                    }
                )
            }
        }
        //에러 났을 때(ex.Network Error)
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    imageItem: PostContentModel,
    page: Int,
    onDoubleTab: (Boolean) -> Unit
) {
    val view = LocalView.current
    val isConcentrateMode = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBarsIgnoringVisibility,
                title = {},
                navigationIcon = {
                    if (!isConcentrateMode.value) Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "뒤로 가기",
                        tint = Color.White
                    )
                },
                actions = {
                    if (isConcentrateMode.value) Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            isConcentrateMode.value = false
                            onDoubleTab(true)
                        },
                        tint = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        if (!isConcentrateMode.value) DetailContent(
            padding = padding,
            imageUrl = imageItem.imageUrl,
            imageDescription = imageItem.description,
            onDoubleTab = {
                isConcentrateMode.value = true
                requestFullScreen(view)
                onDoubleTab(false)
            },
            tagList = imageItem.tags
        ) else {
            ConcentrateModeContent(
                padding = padding,
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
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailContent(
    padding: PaddingValues,
    imageUrl: String,
    imageDescription: String,
    tagList: List<String> = emptyList(),
    onDoubleTab: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isReactionVisible = remember { mutableStateOf(false) }
    val isFollowDialogVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .combinedClickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = {},
                    onDoubleClick = { onDoubleTab() }
                ),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
        Box {
            Column {
                Text(
                    text = imageDescription,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
                val tags = tagList.joinToString(", ") { "#$it" }
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
                        imageVector = Icons.Outlined.FavoriteBorder,
                        tint = Color(0xFF00BBFF),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            isReactionVisible.value = true
                        }
                    )
                    Row {
                        DetailButton(
                            onClick = {},
                            id = R.drawable.ic_detail_gallary,
                            buttonText = "갤러리"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        DetailButton(
                            onClick = { isFollowDialogVisible.value = true },
                            id = R.drawable.ic_detail_follow,
                            buttonText = "팔로우"
                        )
                    }
                }
            }
            if (isReactionVisible.value) ReactionDialog(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                selectedReaction = null,
                onClick = {
                    isReactionVisible.value = !isReactionVisible.value
                },
                onDismiss = { isReactionVisible.value = false }
            )
        }
    }
    if (isFollowDialogVisible.value)
        FollowDialog(
            onClickCancel = { isFollowDialogVisible.value = false }
        )
}

@Composable
fun ConcentrateModeContent(
    padding: PaddingValues,
    imageUrl: String
) {
    var scale by remember {
        mutableStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
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
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
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
    buttonText: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D37))
    ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = null,
            tint = Color(0xFF00BBFF)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = buttonText, color = Color(0xFF00BBFF))
    }
}

fun requestFullScreen(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    insetController.hide(
        WindowInsetsCompat.Type.statusBars() or
                WindowInsetsCompat.Type.navigationBars()
    )
}

fun showSystembar(view: View) {
    // !! should be safe here since the view is part of an Activity
    val window = view.context.getActivity()!!.window
    val insetController = WindowCompat.getInsetsController(window, view)
    insetController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    insetController.show(
        WindowInsetsCompat.Type.statusBars() or
                WindowInsetsCompat.Type.navigationBars()
    )
}


fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DetailScreenPreview() {
    DetailContent(
        padding = PaddingValues(0.dp),
        imageUrl = "https://www.adobe.com/content/dam/cc/us/en/creative-cloud/photography/discover/landscape-photography/CODERED_B1_landscape_P2d_714x348.jpg.img.jpg",
        imageDescription = "집으로 가는 길 풍경 좋다",
        onDoubleTab = {},
        tagList = listOf("풍경", "등산", "가을산")
    )
}