package com.kolown.upload

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kolown.upload.component.CategoryGroup

@Composable
internal fun UploadRoute(
    imgUrl: String,
    padding: PaddingValues
) {
    UploadScreen(
        imgUrl,
        padding = padding
    )
}

@Composable
internal fun UploadScreen(
    imgUrl: String = "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
    padding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        UploadTopAppBar()
        UploadContent(
            imgUrl = imgUrl,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "올리기",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun UploadContent(
    imgUrl: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val ratio = 4f / 5f // todo 이후에 가로 이미지를 지원할 때는 분기처리 필요
        var imageDescription by remember { mutableStateOf("") }
        val horizontalModifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()

        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            modifier = horizontalModifier.aspectRatio(ratio),
            model = imgUrl,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        DescriptionTextField(horizontalModifier, imageDescription) {
            imageDescription = it
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(horizontal = 56.dp),
            text = "설명은 최대 20자까지만 입력이 가능합니다.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(50.dp))
        CategoryGroup(modifier = horizontalModifier, categoryItems = listOf("커피 한잔", "캠핑", "감성 가득"))
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun UploadTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "PostTopAppBar"
            )
        }
        Text(
            text = "새 게시물",
            fontSize = 22.sp
        )
    }
}

@Composable
private fun DescriptionTextField(
    modifier: Modifier,
    imageDescription: String,
    onDescriptionChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        value = imageDescription,
        onValueChange = {
            if (it.length <= 20) {
                onDescriptionChange(it)
            }
        },
        placeholder = { Text(text = "설명 추가...", style = MaterialTheme.typography.bodyLarge) },
        trailingIcon = { TextFieldResetButton { onDescriptionChange("") } }
    )
}

@Composable
private fun TextFieldResetButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Outlined.Clear,
            contentDescription = "trailing icon"
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewUploadScreen() {
    UploadScreen()
}