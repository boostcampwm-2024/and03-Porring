package com.kolown.model

data class ReactionModel(
    val userId: String = "",
    val postId: String = "",
    val reaction: Reactions? = null
)
