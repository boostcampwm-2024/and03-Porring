package com.kolown.my.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState

@Composable
internal fun PageItemFooter(
    modifier: Modifier = Modifier,
    loadState: LoadState,
    onRetryClicked: () -> Unit,
) {

    when (loadState) {
        is LoadState.Loading -> {
            LoadingPageItem()
        }

        is LoadState.Error -> {
            RetryPageItem(modifier, onRetryClicked)
        }

        else -> {}
    }
}

@Composable
private fun LoadingPageItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
    }
}

@Composable
private fun RetryPageItem(modifier: Modifier = Modifier, onRetryClicked: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Retry")
            IconButton(onClick = onRetryClicked) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
            }
        }
    }

}

@Composable
@Preview
private fun LoadingPreview() {
    LoadingPageItem()
}
