package com.kolown.data.datasource.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kolown.data.remote.ReactionDto
import com.kolown.data.remote.toReactionModel
import com.kolown.model.ReactionModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ReactionDataSource {
    suspend fun getReactionByPostId(postId: String): Result<List<ReactionModel>>
}

class ReactionDataSourceImpl @Inject constructor() : ReactionDataSource {
    private val db = Firebase.firestore
    private val reactionCollection = db.collection("reaction")

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
}