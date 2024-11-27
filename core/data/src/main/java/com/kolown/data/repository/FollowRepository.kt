package com.kolown.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.paging.FollowerGalleryThumbnailPagingDataSource
import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.FollowDataSource
import com.kolown.model.FollowerThumbnail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

interface FollowRepository {
    fun getFollowerName(followerId: String): Flow<String>
    suspend fun unFollowUser(followerId: String): Flow<Boolean>
    fun followUser(followerId: String, followerName: String): Flow<Boolean>
    suspend fun getFollowerDataSourcePagingFlow(): Flow<PagingData<FollowerThumbnail>>
}

class FollowRepositoryImpl @Inject constructor(
    private val followDataSource: FollowDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
    private val followerGalleryThumbnailPagingDataSource: FollowerGalleryThumbnailPagingDataSource,
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

    override fun followUser(
        followerId: String,
        followerName: String,
    ): Flow<Boolean> = flow {
        val currentUserId = googleAuthDataSource.getUserId()

        followDataSource.uploadFollow(
            userId = currentUserId,
            followerId = followerId,
            followerName = followerName
        ).collect { success ->
            emit(success)
        }
    }

    override suspend fun unFollowUser(
        followerId: String,
    ): Flow<Boolean> = flow {
        val currentUserId = googleAuthDataSource.getUserId()

        followDataSource.removeFollow(
            userId = currentUserId,
            followerId = followerId
        ).collect { success ->
            emit(success)
        }
    }

    override suspend fun getFollowerDataSourcePagingFlow(): Flow<PagingData<FollowerThumbnail>> {
        return Pager(
            config = PagingConfig(
                pageSize = FOLLOWER_PER_PAGE,
                enablePlaceholders = false,
            ), pagingSourceFactory = {
                followerGalleryThumbnailPagingDataSource
            }
        ).flow
    }

    companion object {
        const val FOLLOWER_PER_PAGE = 5
    }

}