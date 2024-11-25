package com.kolown.data.datasource.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kolown.data.remote.ReactionDto
import com.kolown.data.remote.toReactionModel
import com.kolown.model.ReactionModel
import com.kolown.model.Reactions
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

interface ReactionDataSource {
    suspend fun getReactionByPostId(postId: String): Result<List<ReactionModel>>
    suspend fun updatePostReaction(userId: String, postId: String, reaction: Reactions)
    suspend fun removePostReaction(userId: String, postId: String): Result<Unit>
}

class ReactionDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : ReactionDataSource {
    private val reactionCollection = firestore.collection("reaction")

    override suspend fun getReactionByPostId(postId: String): Result<List<ReactionModel>> {
        return kotlin.runCatching {
            reactionCollection
                .whereEqualTo("postId", postId)
                .get()
                .await()
                .let { snapshot ->
                    if (snapshot.isEmpty) {
                        emptyList()
                    } else {
                        snapshot.map { it.toObject(ReactionDto::class.java).toReactionModel() }
                    }
                }
        }
    }

    override suspend fun updatePostReaction(userId: String, postId: String, reaction: Reactions) {
        val uploadData = mapOf(
            "userId" to userId,
            "postId" to postId,
            "reaction" to reaction.value
        )

        val prevReaction =
            reactionCollection.contains(postId, userId).getOrElse { throw IOException("리액션 에러") }

        if (prevReaction.isEmpty) {
            reactionCollection.add(uploadData).await()
                .let { it.update("reactionId", "reaction-${it.id}") }
        } else {
            prevReaction.forEach {
                it.reference.update("reaction", reaction.value)
            }
        }
    }

    override suspend fun removePostReaction(
        userId: String,
        postId: String,
    ): Result<Unit> {
        return kotlin.runCatching {
            val prevReaction =
                reactionCollection.contains(postId, userId)
                    .getOrElse { throw IOException("리액션 에러") }

            if (prevReaction.isEmpty) return Result.failure(IOException("리액션 없음"))

            prevReaction.forEach { it.reference.delete().await() }
        }
    }

    private suspend fun CollectionReference.contains(
        postId: String,
        userId: String,
    ): Result<QuerySnapshot> {
        return kotlin.runCatching {
            this
                .whereEqualTo("postId", postId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
        }
    }
}