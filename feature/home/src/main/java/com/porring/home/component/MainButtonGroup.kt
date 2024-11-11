package com.porring.home.component

import IconImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun MainButtonGroup(
    onAddClick: () -> Unit,
    onImageClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        modifier = Modifier.fillMaxWidth()
    ) {
        MainButton(Icons.Default.Add, onAddClick)
        MainButton(IconImage, onImageClick)
        MainButton(Icons.Default.Favorite, onLikeClick)
    }
}

@Composable
private fun MainButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(48.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainButton() {
    MainButtonGroup(
        {},
        {},
        {}
    )
}