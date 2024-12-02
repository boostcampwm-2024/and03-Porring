package com.kolown.upload

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kolown.designsystem.R.drawable
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.component.PorringTextField
import com.kolown.designsystem.component.PorringTopAppBar
import com.kolown.designsystem.ui.theme.Error
import com.kolown.designsystem.ui.theme.Gray
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.upload.component.CategoryGroup
import kotlinx.coroutines.launch

@Composable
internal fun UploadRoute(
    viewModel: UploadViewModel = hiltViewModel(),
    imgUri: String,
    padding: PaddingValues,
    navigateToHome: () -> Unit,
    uploadPost: (String, String, List<String>) -> Unit,
) {
    val description by viewModel.description.collectAsStateWithLifecycle()
    val categoryItems by viewModel.categoryItems.collectAsStateWithLifecycle()
    val webPUri by viewModel.webPUri.collectAsStateWithLifecycle()
    val uploadEnable by viewModel.uploadEnable.collectAsStateWithLifecycle()

    BackHandler {
        navigateToHome()
    }

    LaunchedEffect(Unit) {
        if (imgUri.isNotBlank()) {
            viewModel.getUriWebP(imgUri)
        }
    }

    UploadScreen(
        imgUri = if (imgUri == "") {
            webPUri.toString()
        } else {
            imgUri
        },
        padding = padding,
        description = description,
        uploadEnable = uploadEnable,
        changeDescription = viewModel::changeDescription,
        categoryItems = categoryItems,
        addCategory = viewModel::addCategory,
        removeCategory = viewModel::removeCategory,
        changeCategoryName = viewModel::changeCategoryName,
        uploadPost = { uploadPost(webPUri.toString(), description, categoryItems) },
        navigateToHome = navigateToHome
    )
}

@Composable
private fun UploadScreen(
    imgUri: String = "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
    padding: PaddingValues = PaddingValues(),
    description: String = "",
    uploadEnable: Boolean = false,
    changeDescription: (String) -> Unit = {},
    categoryItems: List<String> = emptyList(),
    addCategory: () -> Unit = {},
    removeCategory: (String) -> Unit = {},
    changeCategoryName: (Int, String) -> Unit = { _, _ -> },
    uploadPost: () -> Unit = {},
    navigateToHome: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(imeHeight) {
        coroutineScope.launch {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        PorringTopAppBar(
            title = stringResource(R.string.string_new_post),
            navigationIcon = {
                PorringIconButton(
                    icon = ImageVector.vectorResource(drawable.ic_arrow_back),
                    onClick = navigateToHome,
                    contentDescription = "뒤로 가기"
                )
            }
        )

        UploadContent(
            imgUri = imgUri,
            uploadEnable = uploadEnable,
            description = description,
            changeDescription = changeDescription,
            categoryItems = categoryItems,
            addCategory = addCategory,
            removeCategory = removeCategory,
            changeCategoryName = changeCategoryName,
            uploadPost = uploadPost,
            navigateToHome = navigateToHome
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UploadContent(
    imgUri: String,
    uploadEnable: Boolean,
    description: String,
    changeDescription: (String) -> Unit,
    categoryItems: List<String>,
    addCategory: () -> Unit,
    removeCategory: (String) -> Unit,
    changeCategoryName: (Int, String) -> Unit,
    uploadPost: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var scrollState = rememberScrollState()
    var imeHeightState by remember { mutableStateOf(0) }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(imeHeight) {
        imeHeightState = imeHeight
        scrollState.scrollTo(imeHeight)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .padding(top = 8.dp)
    ) {
        val ratio = 4f / 5f // todo 이후에 가로 이미지를 지원할 때는 분기처리 필요

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio)
                        .clip(shape = RoundedCornerShape(10.dp)),
                    model = imgUri,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "upload image"
                )

                Column {
                    PorringTextField(
                        value = description,
                        onValueChange = changeDescription,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            PorringIconButton(
                                icon = ImageVector.vectorResource(com.kolown.common.R.drawable.ic_cancel),
                                onClick = { changeDescription("") }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (description.length >= 20) {
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = "설명은 최대 20자까지만 입력이 가능합니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Error
                        )
                    }
                }

                CategoryGroup(
                    categoryItems = categoryItems,
                    addCategory = addCategory,
                    removeCategory = removeCategory,
                    changeCategoryName = changeCategoryName
                )
            }
        }


        androidx.compose.animation.AnimatedVisibility(
            visible = imeHeightState < 1,
            enter = fadeIn(),
            exit = ExitTransition.None
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(40.dp),
                onClick = {
                    uploadPost()
                    navigateToHome()
                },
                enabled = uploadEnable,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (uploadEnable) Primary else PrimaryUnActive)
            ) {
                Text(
                    text = "올리기",
                    color = if (uploadEnable) Color.White else Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TextFieldResetButton(
    onClick: () -> Unit,
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