package com.kolown.upload

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kolown.upload.component.CategoryGroup

@Composable
internal fun UploadRoute(
    viewModel: UploadViewModel = hiltViewModel(),
    imgUri: String,
    padding: PaddingValues
) {
    val description by viewModel.description.collectAsStateWithLifecycle()
    val categoryItems by viewModel.categoryItems.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUriWebP(imgUri)
    }

    UploadScreen(
        imgUri,
        padding = padding,
        description,
        viewModel::changeDescription,
        categoryItems,
        viewModel::addCategory,
        viewModel::removeCategory,
        viewModel::changeCategoryName,
        viewModel::uploadPost
    )
}

@Composable
internal fun UploadScreen(
    imgUri: String = "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
    padding: PaddingValues = PaddingValues(),
    description: String = "",
    changeDescription: (String) -> Unit = {},
    categoryItems: List<String> = emptyList(),
    addCategory: () -> Unit = {},
    removeCategory: (String) -> Unit = {},
    changeCategoryName: (Int, String) -> Unit = { _, _ -> },
    uploadPost: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        UploadTopAppBar()
        UploadContent(
            imgUri = imgUri,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            description = description,
            changeDescription = changeDescription,
            categoryItems = categoryItems,
            addCategory = addCategory,
            removeCategory = removeCategory,
            changeCategoryName = changeCategoryName
        )
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            onClick = uploadPost,
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
    imgUri: String,
    modifier: Modifier,
    description: String,
    changeDescription: (String) -> Unit,
    categoryItems: List<String>,
    addCategory: () -> Unit,
    removeCategory: (String) -> Unit,
    changeCategoryName: (Int, String) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val ratio = 4f / 5f // todo 이후에 가로 이미지를 지원할 때는 분기처리 필요
        val horizontalModifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()

        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            modifier = horizontalModifier.aspectRatio(ratio),
            model = imgUri,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        DescriptionTextField(horizontalModifier, description, changeDescription)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(horizontal = 56.dp),
            text = "설명은 최대 20자까지만 입력이 가능합니다.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(50.dp))
        CategoryGroup(
            modifier = horizontalModifier,
            categoryItems = categoryItems,
            addCategory = addCategory,
            removeCategory = removeCategory,
            changeCategoryName = changeCategoryName
        )
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
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        maxLines = 2,
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