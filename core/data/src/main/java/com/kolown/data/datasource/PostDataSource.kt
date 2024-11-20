package com.kolown.data.datasource

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kolown.data.remote.PostDto
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface PostDataSource {
    suspend fun uploadPost(authorId: String, description: String): Result<String>
    suspend fun updateImageUrl(documentId: String, imageUrl: String)
}

class PostDataSourceImpl @Inject constructor() : PostDataSource {
    private val postCollection = Firebase.firestore.collection("post")

    override suspend fun uploadPost(
        authorId: String,
        description: String
    ): Result<String> {
        return runCatching {
            val upload = PostDto(
                authorId = authorId,
                description = description,
                registerAt = LocalDateTime.now().toString(),
            )
            postCollection.add(upload).await().let { documentReference ->
                documentReference.update("postId", "post-${documentReference.id}")
                "post-${documentReference.id}"
            }
        }
    }

    override suspend fun updateImageUrl(documentId: String, imageUrl: String) {
        postCollection.document(documentId).update("imageUrl", imageUrl)
    }
}