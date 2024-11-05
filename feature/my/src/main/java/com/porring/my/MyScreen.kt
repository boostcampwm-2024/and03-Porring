package com.porring.my

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
internal fun MyRoute(
    padding: PaddingValues = PaddingValues(),
) {
    MyScreen(
        padding = padding
    )
}

@Composable
private fun MyScreen(
    padding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = padding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "MyScreen", style = MaterialTheme.typography.displayLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMyScreen() {
    MyScreen()
}