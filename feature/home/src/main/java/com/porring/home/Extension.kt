package com.porring.home

import IconHeart
import IconLove
import IconSmile
import IconStar
import IconSurprise
import IconThumb
import androidx.compose.ui.graphics.vector.ImageVector
import com.kolown.model.Reactions

fun Reactions.toImageVector(): ImageVector =
    when (this) {
        Reactions.LOVE -> IconLove
        Reactions.SURPRISE -> IconSurprise
        Reactions.SMILE -> IconSmile
        Reactions.STAR -> IconStar
        Reactions.THUMB -> IconThumb
        Reactions.HEART -> IconHeart
    }
