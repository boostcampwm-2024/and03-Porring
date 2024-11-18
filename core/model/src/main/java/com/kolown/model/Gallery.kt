package com.kolown.model

data class Gallery(
    val id: Long,
    val postList: List<Post>,
    val name: String,
    val description: String
)
