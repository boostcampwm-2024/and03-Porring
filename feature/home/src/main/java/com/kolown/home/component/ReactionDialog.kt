package com.kolown.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kolown.home.toImage
import com.kolown.model.Reactions

@Composable
internal fun ReactionDialog(
    modifier: Modifier = Modifier,
    selectedReaction: Reactions?,
    onClick: (Reactions) -> Unit,
    onDismiss: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(38.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Reactions.entries.forEach {
                ReactionButton(
                    reaction = it,
                    selectedReaction = selectedReaction,
                    onClick = { reaction ->
                        onClick(reaction)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun ReactionButton(
    reaction: Reactions,
    selectedReaction: Reactions?,
    onClick: (Reactions) -> Unit,
) {
    IconButton(
        modifier = Modifier
            .size(30.dp)
            .background(
                color = if (selectedReaction == reaction) Color.Blue else Color.Transparent,
                shape = CircleShape
            ),
        onClick = { onClick(reaction) }
    ) {
        AsyncImage(
            modifier = Modifier.size(30.dp),
            model = reaction.toImage(),
            contentDescription = null
        )
    }

}