package com.kolown.network

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kolown.model.ReactionModel
import com.kolown.model.Reactions
import com.kolown.network.model.ReactionDto
import com.kolown.network.model.toReactionModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

interface ReactionDataSource {
    suspend fun getReactionByPostId(postId: String): Result<List<ReactionModel>>
    suspend fun updatePostReaction(userId: String, postId: String, reaction: Reactions)
    suspend fun removePostReaction(userId: String, postId: String): Result<Unit>
    suspend fun deletePostReaction(postId: String): Result<Unit>
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

    override suspend fun deletePostReaction(postId: String): Result<Unit> {
        return runCatching {
            val reactions = reactionCollection.whereEqualTo("postId", postId).get().await()

            coroutineScope {
                reactions.documents.map {
                    async {
                        reactionCollection.document(it.id).delete().await()
                    }
                }.awaitAll()
            }
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
