package com.porring.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.kolown.model.Reactions
import com.porring.home.toImage

@Composable
internal fun ReactionGroup(
    modifier: Modifier,
    reactions: List<Reactions>
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
    reaction: Reactions,
    size: Int
) {
    val offset = (-12 * index + (size - 1) * 12).dp

    Card(
        modifier = Modifier
            .size(24.dp)
            .offset(x = offset)
            .zIndex(-index.toFloat())
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = reaction.toImage(),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }

}