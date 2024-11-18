package com.kolown.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun HomeRoute(
    padding: PaddingValues = PaddingValues(),
    onClickImage : ()->Unit,
) {
    HomeScreen(
        padding = padding,
        onClickImage = onClickImage
    )
}

@Composable
private fun HomeScreen(
    padding: PaddingValues = PaddingValues(),
    onClickImage: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = padding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "HomeScreen", style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.clickable {
                onClickImage()
            })
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}
