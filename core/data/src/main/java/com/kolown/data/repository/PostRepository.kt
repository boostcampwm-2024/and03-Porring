package com.kolown.data.repository

import com.kolown.data.datasource.ImageDataSource
import javax.inject.Inject

interface PostRepository {
    suspend fun uploadPost(fileUri: String, description: String, tags: List<String>): Result<Unit>
}

class PostRepositoryImpl @Inject constructor(
    private val imageDataSource: ImageDataSource,
) : PostRepository {
    private val authorId = "user-1feIeEN3rMh4ZY7YpQKxDfnKGvi2"

    override suspend fun uploadPost(
        fileUri: String,
        description: String,
        tags: List<String>
    ): Result<Unit> {
        return runCatching {
            val imageUri = imageDataSource.getImageUrl(authorId, fileUri).getOrThrow()

        }
    }

}