package com.kolown.data.repository

import android.util.Log
import com.kolown.data.datasource.AuthDataSource
import com.kolown.data.datasource.remote.FollowDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

interface FollowRepository {
    fun getFollowerName(followerId: String): Flow<String>
}

class FollowRepositoryImpl @Inject constructor(
    private val followDataSource: FollowDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
) : FollowRepository {

    override fun getFollowerName(followerId: String): Flow<String> = flow {
        val currentUserId = googleAuthDataSource.getUserId()

        followDataSource.getFollowerName(currentUserId, followerId)
            .collect { name ->
                emit(name)
            }
    }.catch { e ->
        Log.e("GetFollowerName", "repository: $e")
    }

}