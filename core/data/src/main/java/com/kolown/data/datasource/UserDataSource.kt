package com.kolown.data.datasource

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kolown.data.remote.UserDto
import javax.inject.Inject

interface UserDataSource {
    suspend fun createUserData(userDto: UserDto): Result<Unit>
}

class UserDataSourceImpl @Inject constructor() : UserDataSource {
    private val userCollection by lazy { Firebase.firestore.collection("user") }

    override suspend fun createUserData(userDto: UserDto): Result<Unit> {
        return kotlin.runCatching {
            userCollection.document(userDto.userId).set(userDto)
        }
    }
}