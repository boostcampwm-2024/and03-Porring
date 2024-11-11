package com.kolown.model

data class ImageItem(
    val id: Long = -1,
    val user: String = "",
    val imageUrl: String = "",
    val content: String = "",
    val reactions: Reactions? = null,
    val isFollowed: Boolean = false,
    val favoriteList: List<Reactions> = emptyList()
)
