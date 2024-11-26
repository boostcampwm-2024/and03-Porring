package com.kolown.data.repository

import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.UserDataSource
import javax.inject.Inject
import javax.inject.Named

interface UserRepository {
    suspend fun createUserData(): Result<Unit>
}

class UserRepositoryImpl @Inject constructor(
    @Named("google") private val authDataSource: AuthDataSource,
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun createUserData(): Result<Unit> {
        return kotlin.runCatching {
            authDataSource.getUserInfo().let {
                userDataSource.createUserData(it)
            }
        }
    }
}