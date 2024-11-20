package com.kolown.data.datasource.remote

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kolown.data.remote.PostDto
import com.kolown.data.remote.toPostModel
import com.kolown.model.PostModel
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface PostDataSource {
    suspend fun uploadPost(authorId: String, description: String): Result<String>
    suspend fun updateImageUrl(documentId: String, imageUrl: String)
    suspend fun getRandomPost(uid: String, count: Int): Result<List<Any>>
}

class PostDataSourceImpl @Inject constructor() : PostDataSource {
    private val postCollection = Firebase.firestore.collection("post")
    private val randomType = listOf("A", "B", "C", "D", "E").random()

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

    override suspend fun getRandomPost(uid: String, count: Int): Result<List<PostModel>> {
        val randomValue = (0..Long.MAX_VALUE).random()

        return kotlin.runCatching {
            val fetchPosts: suspend (Long) -> List<PostModel> = { key ->
                postCollection
                    .whereNotEqualTo("authorId", uid)
                    .whereGreaterThan("random$randomType", key)
                    .orderBy("random$randomType", Query.Direction.ASCENDING)
                    .limit(count.toLong())
                    .get()
                    .addOnFailureListener {
                    }
                    .await()
                    .map { it.toObject(PostDto::class.java).toPostModel(randomType) }
            }
            val result = fetchPosts(randomValue)

            if (result.size > count) result else fetchPosts(0)
        }
    }
}