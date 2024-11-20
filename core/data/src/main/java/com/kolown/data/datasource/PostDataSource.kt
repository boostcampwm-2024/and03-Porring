package com.kolown.data.datasource

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kolown.data.remote.PostDto
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

interface PostDataSource {
    suspend fun uploadPost(authorId: String, imageUri: String, description: String): Result<String>
}

class PostDataSourceImpl : PostDataSource {
    private val postCollection = Firebase.firestore.collection("post")

    override suspend fun uploadPost(
        authorId: String,
        imageUri: String,
        description: String
    ): Result<String> {
        return runCatching {
            val upload = PostDto(
                authorId = authorId,
                imageUrl = imageUri,
                description = description,
                registerAt = LocalDateTime.now().toString(),
            )
            postCollection.add(upload).await().let { documentReference ->
                documentReference.update("postId", "post-${documentReference.id}")
                "post-${documentReference.id}"
            }
        }
    }
}