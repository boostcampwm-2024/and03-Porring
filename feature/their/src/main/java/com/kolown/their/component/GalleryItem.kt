package com.kolown.their.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kolown.data.mock.MockDataProvider
import com.kolown.model.GalleryThumbnail
import com.kolown.model.PostContentModel
import kotlin.random.Random

@Composable
internal fun GalleryItem(galleryThumbnail: PostContentModel, width: Dp) {
    //비율은 그냥 테스트
    val height = if (Random.nextBoolean()) (width.value * 1.4).dp else width + 20.dp

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(10.dp)),
        model = galleryThumbnail.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

}

//@Preview
//@Composable
//private fun GalleryItemPreview() {
//    GalleryItem(MockDataProvider.getRandomGalleryThumbnail(), 200.dp)
//}
