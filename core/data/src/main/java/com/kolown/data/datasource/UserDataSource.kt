package com.kolown.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kolown.data.remote.UserDto
import javax.inject.Inject

interface UserDataSource {
    suspend fun createUserData(userDto: UserDto): Result<Unit>
}

class UserDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore
) : UserDataSource {
    private val userCollection by lazy { firestore.collection("user") }

    override suspend fun createUserData(userDto: UserDto): Result<Unit> {
        return kotlin.runCatching {
            userCollection.document(userDto.userId).set(userDto)
        }
    }
}