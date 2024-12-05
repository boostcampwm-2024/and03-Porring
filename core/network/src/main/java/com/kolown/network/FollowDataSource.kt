package com.kolown.network

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.kolown.data.remote.FollowerDto
import com.kolown.data.remote.toFollowerModel
import com.kolown.model.FollowerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

interface FollowDataSource {
    suspend fun getIsFollower(userId: String, followerId: String): Result<Boolean>
    suspend fun uploadFollow(
        userId: String,
        followerId: String,
        followerName: String
    ): Flow<Boolean>

    suspend fun removeFollow(userId: String, followerId: String): Flow<Boolean>
    suspend fun getFollowerName(userId: String, followerId: String): Flow<String>
    suspend fun getFollowerList(
        userId: String,
        key: String?,
        perPage: Long
    ): Result<List<FollowerModel>>
}

class FollowDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore
) : FollowDataSource {
    private val followCollection = firestore.collection("follow")

    override suspend fun getIsFollower(userId: String, followerId: String): Result<Boolean> {
        return runCatching {
            val result = followCollection.contains(userId, followerId).getOrElse {
                throw IOException("팔로우 확인 에러")
            }

            result.isEmpty.not()
        }.onFailure {
            Log.e("GetFollower", "getIsFollower: $it")
        }
    }

    override suspend fun getFollowerName(userId: String, followerId: String): Flow<String> = flow {

        val prevFollow = followCollection.contains(userId, followerId).getOrElse {
            throw IOException("팔로우 확인 에러")
        }

        if (prevFollow.isEmpty) {
            throw IOException("팔로우 관계가 없습니다.")
        }

        val name = prevFollow.first().data["followerName"].toString()

        emit(name)
    }

    override suspend fun getFollowerList(
        userId: String,
        key: String?,
        perPage: Long
    ): Result<List<FollowerModel>> {
        return kotlin.runCatching {
            val followerIds = followCollection
                .whereEqualTo("userId", userId)
                .orderBy("followerId", Query.Direction.DESCENDING)
                .let { if (key != null) it.startAfter(key) else it }
                .limit(perPage)
                .get()
                .await()
                .map { it.toObject(FollowerDto::class.java).toFollowerModel() }

            followerIds
        }
    }

    override suspend fun uploadFollow(
        userId: String,
        followerId: String,
        followerName: String
    ): Flow<Boolean> = flow {
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
            .runCatching {
                emit(true)
            }.onFailure { e ->
                emit(false)
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
            Log.e("FollowContains", "contains: $it")
        }
    }
}
