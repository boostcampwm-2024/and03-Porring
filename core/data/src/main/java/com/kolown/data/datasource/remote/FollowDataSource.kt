package com.kolown.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FollowDataSource {
    suspend fun uploadFollow(userId: String, followerId: String, followerName: String)
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
}