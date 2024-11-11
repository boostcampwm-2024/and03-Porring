package com.porring.home.component

import IconFollow
import IconGallery
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kolown.model.ImageItem

@Composable
internal fun RandomImageList(
    pagerState: PagerState = rememberPagerState(pageCount = { 10 }),
    imageItems: List<ImageItem> = emptyList(),
    onFollowClick: (Long) -> Unit = {},
) {
    HorizontalPager(
        modifier = Modifier.padding(top = 32.dp),
        state = pagerState
    ) { page ->
        ImageCard(imageItems[page], page, onFollowClick)
    }
}

@Composable
private fun ImageCard(
    imageItem: ImageItem,
    idx: Int,
    onFollowClick: (Long) -> Unit
) {
    val likedImageVector =
        if (imageItem.reactions == null) Icons.Outlined.FavoriteBorder else Icons.Outlined.Favorite

    Column {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .aspectRatio(4f / 5f),
            contentAlignment = Alignment.Center
        ) {
            RandomImage(imageItem.imageUrl)
            ReactionGroup(
                modifier = Modifier.align(Alignment.TopEnd),
                reactions = imageItem.favoriteList
            )
            Text(
                text = "idx: $idx"
            )
            IconButtonGroup(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                imageItem,
                onFollowClick
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            onClick = {},
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = likedImageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun IconButtonGroup(
    modifier: Modifier,
    imageItem: ImageItem,
    onFollowClick: (Long) -> Unit
) {
    val whiteModifier = Modifier
        .clip(CircleShape)
        .size(48.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomIconButton(
            whiteModifier,
            IconFollow,
            imageItem.isFollowed
        ) {
            onFollowClick(imageItem.id)
        }
        CustomIconButton(
            whiteModifier,
            IconGallery
        ) {

        }
    }
}

@Composable
private fun RandomImage(
    imageUrl: String = "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png"
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null
        )
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
        modifier = modifier.background(if (isSelected) MaterialTheme.colorScheme.primary else Color.White),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRandomImageList() {
    RandomImageList()
}