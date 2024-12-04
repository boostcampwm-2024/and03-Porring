package com.kolown.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kolown.common.component.shimmerEffect
import com.kolown.model.Post
import com.kolown.model.PostContentModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostItem(
    post: PostContentModel,
    onClick: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val loadingModifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(color = Color.LightGray)
        .aspectRatio(1f)
        .shimmerEffect()
    val successModifier =
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .clickable {
                onClick()
            }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = if(isLoading.value) loadingModifier else successModifier
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current).data(post.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onLoading = {
                isLoading.value = true
            },
            onSuccess = {
                coroutineScope.launch {
                    delay(1500)
                    isLoading.value = false
                }
            }
        )
    }
}
