package com.kolown.common.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun NoRippleCoilImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    delay: Long = 0L,
    imageUrl: String,
    isTextExist: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val loadingModifier = modifier.shimmerEffect()

    if (isError) {
        Column(
            modifier = modifier
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
    } else {
        Box(
            modifier = if (isLoading) loadingModifier else modifier,
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onClick,
                        onLongClick = onLongClick,
                        onDoubleClick = onDoubleClick
                    ),
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
                    isLoading = false
                    isError = true
                }
            )
        }
    }

}