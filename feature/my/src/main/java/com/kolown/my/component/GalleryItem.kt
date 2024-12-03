package com.kolown.my.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kolown.data.mock.MockDataProvider
import com.kolown.designsystem.ui.theme.Surface2
import com.kolown.model.PostContentModel
import com.kolown.my.R
import kotlin.math.absoluteValue
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GalleryItem(
    postContentModel: PostContentModel,
    width: Dp,
    onLongClickImage: () -> Unit = {},
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val heightNum = postContentModel.postId.filter { it.isDigit() }
        .takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
    val height = if (heightNum % 2 == 0) (width.value * 1.4).dp else width + 20.dp
    val isDialogVisible = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(10.dp))
            .background(Surface2)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = {
                        // todo navigateToDetail
                    },
                    onLongClick = {
                        isDialogVisible.value = true
                    }
                ),
            model = postContentModel.imageUrl,
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

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }

        if (isError) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.string_can_not_load),
                style = MaterialTheme.typography.labelMedium,
            )
        }

        if (isDialogVisible.value) {
            DeleteDialog(
                onClickCancel = { isDialogVisible.value = false },
                onClickConfirm = {
                    onLongClickImage()
                    isDialogVisible.value = false
                }
            )
        }
    }
}

@Preview
@Composable
private fun GalleryItemPreview() {
    GalleryItem(MockDataProvider.getRandomGalleryPostContentModel(), 200.dp)
}
