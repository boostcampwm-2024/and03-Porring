package com.kolown.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kolown.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class RemoteUserDataSource @Inject constructor(
    firestore: FirebaseFirestore,
) {
    private val userCollection by lazy { firestore.collection("user") }

    suspend fun createUserData(user: User): Result<Unit> {
        return kotlin.runCatching {
            userCollection.document(user.userId).set(user)
        }
    }

    suspend fun getUserEmail(userId: String): Flow<String> {
        return flow {
            emit(userCollection.document(userId).get().await().getString("email").orEmpty())
        }
    }
}
