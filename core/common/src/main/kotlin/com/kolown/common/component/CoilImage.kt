package com.kolown.common.component

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kolown.common.R
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.designsystem.ui.theme.Surface2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoilImage(
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    delay: Long = 0L,
    imageUrl: String,
    isTextExist: Boolean = true,
    onClickEnabled: Boolean = true
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val loadingModifier = Modifier
        .fillMaxSize()
        .shimmerEffect()
    val successModifier =
        if (onClickEnabled) Modifier
            .fillMaxSize()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
        else Modifier.fillMaxSize()

    AsyncImage(
        modifier = if (isLoading) loadingModifier else successModifier,
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        onLoading = {
            isLoading = true
            isError = false
        },
        onSuccess = {
            coroutineScope.launch {
                delay(delay)
                isLoading = false
                isError = false
            }
        },
        onError = {
            Log.e("에러", "")
            isLoading = false
            isError = true
        }
    )

    if (isError) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Surface2),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier.fillMaxSize(1 / 2f),
                painter = painterResource(R.drawable.icon_lost_image),
                tint = PrimaryUnActive,
                contentDescription = "no_image"
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (isTextExist) {
                Text(
                    text = stringResource(R.string.string_can_not_load),
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary
                )
            }
        }
    }

}