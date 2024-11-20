package com.kolown.model

import com.kolown.model.Reactions.entries

enum class Reactions(val value: Int) {
    LOVE(0), SURPRISE(1), SMILE(2), STAR(3), THUMB(4), HEART(5);
}

fun Int.toReactions(): Reactions {
    return entries.first { it.value == this }
}