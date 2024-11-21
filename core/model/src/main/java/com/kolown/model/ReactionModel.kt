package com.kolown.model

import kotlinx.serialization.Serializable

@Serializable
data class ReactionModel(
    val userId: String = "",
    val postId: String = "",
    val reaction: Reactions? = null
)
