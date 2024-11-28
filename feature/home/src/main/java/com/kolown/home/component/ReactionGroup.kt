package com.kolown.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kolown.home.toImage
import com.kolown.model.Reactions

@Composable
internal fun ReactionGroup(
    modifier: Modifier,
    reactions: List<Reactions>,
) {
    val reactionList = reactions.distinct().sortedBy { it.ordinal }

    if (reactionList.isNotEmpty()) {
        Row(
            modifier = modifier.padding(16.dp)
        ) {
            reactionList.forEachIndexed { index, reaction ->
                ReactionIcons(index, reaction, reactionList.size)
            }
        }
    }
}

@Composable
private fun ReactionIcons(
    index: Int,
    reaction: Reactions,
    size: Int,
) {
    val offset = (-15 * index + (size - 1) * 15).dp

    Card(
        modifier = Modifier
            .size(30.dp)
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
            contentDescription = "reaction icon",
            modifier = Modifier.fillMaxSize()
        )
    }
}