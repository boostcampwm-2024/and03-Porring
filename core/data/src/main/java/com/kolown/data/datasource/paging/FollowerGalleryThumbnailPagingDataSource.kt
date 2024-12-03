package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.FollowDataSource
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.model.FollowerThumbnail
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class FollowerGalleryThumbnailPagingDataSource @Inject constructor(
    private val followerDataSource: FollowDataSource,
    private val postDataSource: PostDataSource,
    private val currentUserId: String
) : PagingSource<String, FollowerThumbnail>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, FollowerThumbnail> {
        return try {
            val key = params.key

            val followers = followerDataSource.getFollowerList(
                userId = currentUserId,
                key = key,
                perPage = params.loadSize.toLong()
            ).getOrElse { throw Exception("팔로워 불러오기 실패") }

            val thumbnails = coroutineScope {
                followers.map { follower ->
                    async {
                        val posts =
                            postDataSource.getUserFollowerPost(
                                uid = follower.followerId,
                                perPage = 3
                            ).getOrElse { throw Exception("게시물 불러오기 실패") }

                        FollowerThumbnail(
                            id = follower.followerId,
                            followerName = follower.followerName,
                            posts = posts.map { it.imageUrl }
                        )
                    }
                }.awaitAll()
            }
            LoadResult.Page(
                data = thumbnails,
                prevKey = if (key == null) null else followers.firstOrNull()?.followerId,
                nextKey = if (followers.isEmpty()) null else followers.lastOrNull()?.followerId
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, FollowerThumbnail>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}