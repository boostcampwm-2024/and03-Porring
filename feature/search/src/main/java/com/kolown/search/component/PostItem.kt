package com.kolown.search.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kolown.common.component.CoilImage
import com.kolown.model.PostContentModel

@Composable
fun PostItem(
    post: PostContentModel,
    onClick: () -> Unit
) {

    CoilImage(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f),
        onClick = onClick,
        imageUrl = post.imageUrl,
        delay = 1500,
        isTextExist = false
    )

}
