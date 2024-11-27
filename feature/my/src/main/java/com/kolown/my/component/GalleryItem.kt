package com.kolown.my.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.kolown.data.mock.MockDataProvider
import com.kolown.designsystem.Error
import com.kolown.designsystem.Primary
import com.kolown.model.PostContentModel
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryItem(
    postContentModel: PostContentModel,
    width: Dp,
    onLongClickImage: () -> Unit = {}
) {
    //비율은 그냥 테스트
    val heightNum = postContentModel.postId.filter { it.isDigit() }.toInt()
    val height = if (heightNum % 2 == 0) (width.value * 1.4).dp else width + 20.dp
    val isDialogVisible = remember { mutableStateOf(false) }

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable (
                onClick = {
                    // todo navigateToDetail
                },
                onLongClick = {
                    isDialogVisible.value = true
                    Log.d("GalleryItem", "GalleryItem: LongClick")
                }
            ),
        model = postContentModel.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    if(isDialogVisible.value) {
        DeleteDialog(
            onClickCancel = { isDialogVisible.value = false },
            onClickConfirm = {
                onLongClickImage()
                isDialogVisible.value = false
            }
        )
    }

}

@Preview
@Composable
private fun GalleryItemPreview() {
    GalleryItem(MockDataProvider.getRandomGalleryPostContentModel(), 200.dp)
}
