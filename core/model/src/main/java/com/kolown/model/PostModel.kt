package com.kolown.model

data class PostModel(
    val postId: String,
    val authorId: String,
    val imageUrl: String,
    val registerAt: String,
    val description: String,
    val random: Long = 0L
)
