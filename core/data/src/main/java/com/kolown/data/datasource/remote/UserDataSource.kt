package com.kolown.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kolown.data.remote.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserDataSource {
    suspend fun createUserData(userDto: UserDto): Result<Unit>
    suspend fun getUserEmail(userId: String): Flow<String>
}

class UserDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : UserDataSource {
    private val userCollection by lazy { firestore.collection("user") }

    override suspend fun createUserData(userDto: UserDto): Result<Unit> {
        return kotlin.runCatching {
            userCollection.document(userDto.userId).set(userDto)
        }
    }

    override suspend fun getUserEmail(userId: String): Flow<String> {
        return flow {
            emit(userCollection.document(userId).get().await().getString("email").orEmpty())
        }
    }
}