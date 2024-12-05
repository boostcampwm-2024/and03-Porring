package com.kolown.network.model

import com.kolown.model.PostModel
import com.kolown.network.Util.randomValue

data class PostDto(
    val postId: String = "",
    val authorId: String = "",
    val imageUrl: String = "",
    val registerAt: String = "",
    val description: String = "",
    val randomA: Long = randomValue(),
    val randomB: Long = randomValue(),
    val randomC: Long = randomValue(),
    val randomD: Long = randomValue(),
    val randomE: Long = randomValue(),
)

fun PostDto.toPostModel(seed: String = "A"): PostModel {
    return PostModel(
        postId = this.postId,
        authorId = this.authorId,
        imageUrl = this.imageUrl,
        registerAt = this.registerAt,
        description = this.description,
        random = when (seed) {
            "A" -> randomA
            "B" -> randomB
            "C" -> randomC
            "D" -> randomD
            "E" -> randomE
            else -> throw IllegalArgumentException("존재하지 않는 랜덤 시드")
        }
    )
}
