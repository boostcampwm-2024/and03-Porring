package com.kolown.model

data class PostContentModel(
    val authorId: String,
    val imageUrl: String,
    val registerAt: String,
    val description: String,
    val tags: List<String>,
    val isFollower: Boolean,
    val reactions: List<ReactionModel>,
    val myReaction: ReactionModel? = null
)