package com.kolown.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.paging.FollowerGalleryThumbnailPagingDataSource
import com.kolown.network.AuthDataSource
import com.kolown.network.FollowDataSource
import com.kolown.network.PostDataSource
import com.kolown.model.FollowerThumbnail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

interface FollowRepository {
    suspend fun getFollowerName(followerId: String): Flow<String>
    suspend fun unFollowUser(followerId: String): Flow<Boolean>
    fun followUser(followerId: String, followerName: String): Flow<Boolean>
    fun getFollowerDataSourcePagingFlow(): Flow<PagingData<FollowerThumbnail>>
}

class FollowRepositoryImpl @Inject constructor(
    private val followDataSource: FollowDataSource,
    private val followerDataSource: FollowDataSource,
    private val postDataSource: PostDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
) : FollowRepository {

    override suspend fun getFollowerName(followerId: String): Flow<String> {
        val currentUserId = googleAuthDataSource.getUserId()

        return followDataSource.getFollowerName(currentUserId, followerId)
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

    override fun getFollowerDataSourcePagingFlow(): Flow<PagingData<FollowerThumbnail>> {
        val currentUserId = googleAuthDataSource.getUserId()

        return Pager(
            config = PagingConfig(
                pageSize = FOLLOWER_PER_PAGE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                FollowerGalleryThumbnailPagingDataSource(
                    followerDataSource,
                    postDataSource,
                    currentUserId
                )
            }
        ).flow
    }

    companion object {
        const val FOLLOWER_PER_PAGE = 5
    }

}
