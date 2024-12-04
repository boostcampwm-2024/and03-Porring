package com.kolown.data.repository

import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.UserDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

interface UserRepository {
    suspend fun createUserData(): Result<Unit>
    fun checkUserId(authorId: String): Boolean
    suspend fun getLatestUserEmail(): Flow<String>
    fun getUserData(): Result<String>
}

class UserRepositoryImpl @Inject constructor(
    @Named("google") private val authDataSource: AuthDataSource,
    @Named("google") private val userDataSource: UserDataSource,
    @Named("LDS") private val localUserDataSource: UserDataSource,
) : UserRepository {
    override suspend fun createUserData(): Result<Unit> {
        return kotlin.runCatching {
            authDataSource.getUserInfo().let {
                userDataSource.createUserData(it)
            }
        }
    }

    override fun checkUserId(authorId: String): Boolean {
        return authDataSource.getUserId() == authorId
    }

    override suspend fun getLatestUserEmail(): Flow<String> {
        return localUserDataSource.getUserEmail(authDataSource.getUserId())
    }

    override fun getUserData(): Result<String> {
        return runCatching {
            authDataSource.getUserId()
        }
    }
}