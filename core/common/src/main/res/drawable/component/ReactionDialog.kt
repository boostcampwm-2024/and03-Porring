package com.kolown.common.component

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
import com.kolown.designsystem.PrimaryUnActive
import com.kolown.detail.R
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions

@Composable
internal fun ReactionDialog(
    modifier: Modifier = Modifier,
    imageItem: PostContentModel,
    updateMainPostReaction: (PostContentModel, Reactions) -> Unit,
    selectedReaction: (PostContentModel, Reactions) -> Unit,
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
                    didIReact = imageItem.myReaction == it,
                    reaction = it,
                    onClick = { reaction ->
                        updateMainPostReaction(imageItem, reaction)
                        selectedReaction(imageItem, reaction)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun ReactionButton(
    didIReact: Boolean,
    reaction: Reactions,
    onClick: (Reactions) -> Unit,
) {
    IconButton(
        modifier = Modifier
            .size(30.dp)
            .background(
                color = if (didIReact) PrimaryUnActive else Color.Transparent,
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

fun Reactions.toImage() =
    when (this) {
        Reactions.LOVE -> R.drawable.img_love
        Reactions.SURPRISE -> R.drawable.img_surprise
        Reactions.SMILE -> R.drawable.img_smile
        Reactions.STAR -> R.drawable.img_star
        Reactions.THUMB -> R.drawable.img_thumb
        Reactions.HEART -> R.drawable.img_heart
    }
