package com.porring.home.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
internal fun FavoriteGroup(
    modifier: Modifier,
    reactions: List<Int>
) {
    if (reactions.isNotEmpty()) {
        Row(
            modifier = modifier.padding(16.dp)
        ) {
            reactions.forEachIndexed { index, reaction ->
                ReactionIcons(index, reaction, reactions.size)
            }
        }
    }
}

@Composable
private fun ReactionIcons(
    index: Int,
    reaction: Int,
    size: Int
) {
    val offset = (-12 * index + (size - 1) * 12).dp
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .offset(x = offset)
            .zIndex(-index.toFloat())
            .background(
                color = Color.White,
                shape = CircleShape
            )
    )
}