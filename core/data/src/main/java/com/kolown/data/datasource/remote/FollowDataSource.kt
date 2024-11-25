package com.kolown.data.datasource.remote

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

interface FollowDataSource {
    suspend fun uploadFollow(userId: String, followerId: String, followerName: String)
    suspend fun removeFollow(userId: String, followerId: String): Flow<Boolean>
}

class FollowDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore
) : FollowDataSource {
    private val followCollection = firestore.collection("follow")

    override suspend fun uploadFollow(userId: String, followerId: String, followerName: String) {
        val uploadData = mapOf(
            "userId" to userId,
            "followerId" to followerId,
            "followerName" to followerName
        )

        followCollection
            .add(uploadData)
            .await()
            .let {
                it.update("followId", "follow-${it.id}")
            }
    }

    override suspend fun removeFollow(userId: String, followerId: String): Flow<Boolean> = flow {
        val prevFollow = followCollection.contains(userId, followerId).getOrElse {
            throw IOException("팔로우 취소 에러")
        }

        if (prevFollow.isEmpty) {
            throw IOException("팔로우 관계가 없습니다.")
        }

        prevFollow.forEach { document ->
            document.reference.delete().await()
        }

        emit(true)
    }.catch { e ->
        emit(false)
    }

    private suspend fun CollectionReference.contains(
        userId: String,
        followerId: String,
    ): Result<QuerySnapshot> {
        return runCatching {
            this
                .whereEqualTo("userId", userId)
                .whereEqualTo("followerId", followerId)
                .get()
                .await()
        }.onFailure {
            Log.e("FollowTest", "contains: $it")
        }
    }
}