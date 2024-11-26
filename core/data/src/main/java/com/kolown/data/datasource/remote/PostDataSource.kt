package com.kolown.data.datasource.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kolown.data.remote.PostDto
import com.kolown.data.remote.toPostModel
import com.kolown.model.PostModel
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface PostDataSource {
    suspend fun uploadPost(authorId: String, description: String): Result<String>
    suspend fun updateImageUrl(documentId: String, imageUrl: String)
    suspend fun getRandomPost(uid: String, count: Int): Result<List<PostModel>>
    suspend fun getRandomPost(uid: String, page: Long, perPage: Long): Result<List<PostModel>>
    suspend fun getUserPost(uid: String, perPage: Long): Result<List<PostModel>>
    suspend fun getPostBySearch(
        postIds: List<String>,
        lastRegisteredAt: String?,
        perPage: Long
    ): Result<List<PostModel>>
}

class PostDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore
) : PostDataSource {
    private val postCollection = firestore.collection("post")
    private val randomType = listOf("A", "B", "C", "D", "E").random()
    private var lastVisible: DocumentSnapshot? = null

    override suspend fun getUserPost(uid: String, perPage: Long): Result<List<PostModel>> {
        return kotlin.runCatching {
            if (lastVisible == null) {
                postCollection
                    .whereEqualTo("authorId", uid)
                    .orderBy("registerAt", Query.Direction.DESCENDING)
                    .limit(perPage)
                    .get()
                    .await()
                    .also { lastVisible = it.documents.last() }
                    .map { it.toObject(PostDto::class.java).toPostModel() }
            } else {
                postCollection
                    .whereEqualTo("authorId", uid)
                    .orderBy("registerAt", Query.Direction.DESCENDING)
                    .startAfter(lastVisible!!)
                    .limit(perPage)
                    .get()
                    .await()
                    .also { lastVisible = it.documents.lastOrNull() }
                    .map { it.toObject(PostDto::class.java).toPostModel() }
            }
        }
    }

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

        return runCatching {
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

    override suspend fun getRandomPost(
        uid: String,
        page: Long,
        perPage: Long
    ): Result<List<PostModel>> {
        return kotlin.runCatching {
            val fetchPosts: suspend (Long) -> List<PostModel> = { key ->
                postCollection
                    .whereNotEqualTo("authorId", uid)
                    .whereGreaterThan("random$randomType", key)
                    .orderBy("random$randomType", Query.Direction.ASCENDING)
                    .limit(perPage)
                    .get()
                    .await()
                    .map { it.toObject(PostDto::class.java).toPostModel(randomType) }
            }

            fetchPosts(page).ifEmpty { fetchPosts(0) }
        }
    }

    override suspend fun getPostBySearch(
        postIds: List<String>,
        key: String?,
        perPage: Long
    ): Result<List<PostModel>> {
        return kotlin.runCatching {
            // 쿼리 초기화
            val query = postCollection
                .whereIn("postId", postIds)
                .orderBy("postId", Query.Direction.ASCENDING)
                .let { if (key != null) it.startAfter(key) else it }
                .limit(perPage)
                .get()
                .await()
                .mapNotNull { it.toObject(PostDto::class.java).toPostModel(randomType) }

            query
        }
    }


}