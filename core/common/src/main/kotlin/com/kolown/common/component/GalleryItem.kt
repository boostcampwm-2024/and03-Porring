package com.kolown.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.kolown.common.R
import com.kolown.designsystem.ui.theme.Error
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.Surface2
import com.kolown.model.PostContentModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryItem(
    postContentModel: PostContentModel,
    width: Dp,
    onLongClickImage: () -> Unit = {},
    onClickImage: () -> Unit = {}
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
                        onClickImage()
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

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    onClickCancel: () -> Unit = {},
    onClickConfirm: (String) -> Unit = {},
) {
    val textValue = remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            modifier = modifier.size(
                width = 250.dp,
                height = 130.dp
            ),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.string_question_delete),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextButton(
                        onClick = onClickCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = stringResource(R.string.string_cancel), color = Error)
                    }
                    TextButton(
                        onClick = {
                            onClickConfirm(textValue.value)
                            onClickCancel()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = stringResource(R.string.string_remove), color = Primary)
                    }
                }
            }
        }
    }
}
