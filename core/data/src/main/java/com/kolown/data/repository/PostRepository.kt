package com.kolown.data.repository

import com.kolown.data.datasource.ImageDataSource
import com.kolown.data.datasource.PostDataSource
import javax.inject.Inject

interface PostRepository {
    suspend fun uploadPost(fileUri: String, description: String, tags: List<String>): Result<Unit>
}

class PostRepositoryImpl @Inject constructor(
    private val imageDataSource: ImageDataSource,
    private val postDataSource: PostDataSource,
) : PostRepository {
    private val authorId = "user-1feIeEN3rMh4ZY7YpQKxDfnKGvi2"

    override suspend fun uploadPost(
        fileUri: String,
        description: String,
        tags: List<String>
    ): Result<Unit> {
        return runCatching {
            // Image File Upload & get Image Uri
            val imageUri = imageDataSource.getImageUrl(authorId, fileUri).getOrThrow()
            // post Upload to Firestore & get postId
            val postId = postDataSource.uploadPost(authorId, imageUri, description).getOrThrow()
        }
    }

}