package com.kolown.home

import com.kolown.model.Reactions

fun Reactions.toImage() =
    when (this) {
        Reactions.LOVE -> R.drawable.img_love
        Reactions.SURPRISE -> R.drawable.img_surprise
        Reactions.SMILE -> R.drawable.img_smile
        Reactions.STAR -> R.drawable.img_star
        Reactions.THUMB -> R.drawable.img_thumb
        Reactions.HEART -> R.drawable.img_heart
    }