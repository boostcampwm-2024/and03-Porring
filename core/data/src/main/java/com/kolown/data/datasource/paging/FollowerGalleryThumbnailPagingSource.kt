package com.kolown.data.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.fake.FollowerDataSource
import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.FollowDataSource
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.model.FollowerThumbnail
import com.kolown.model.PostContentModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class FollowerGalleryThumbnailPagingDataSource @Inject constructor(
    private val followerDataSource: FollowDataSource,
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDataSource,
    private val reactionDataSource: ReactionDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
) : PagingSource<String, FollowerThumbnail>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, FollowerThumbnail> {
        return try {
            val currentUserId = googleAuthDataSource.getUserId()
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
                            postDataSource.getUserFollowerPost(uid = follower.followerId, perPage = 3)
                                .getOrElse { throw Exception("게시물 불러오기 실패") }
                        val (tags, reactions) = coroutineScope {
                            val tagsDeferred = async {
                                posts.map {
                                    async {
                                        tagDataSource.getPostTag(it.postId).getOrElse {
                                            throw IOException("태그 불러오기 실패")
                                        }
                                    }
                                }.awaitAll()
                            }
                            val reactionsDeferred = async {
                                posts.map {
                                    async {
                                        reactionDataSource.getReactionByPostId(it.postId)
                                            .getOrElse {
                                                throw IOException("리액션 불러오기 실패")
                                            }
                                    }
                                }.awaitAll()
                            }

                            tagsDeferred.await() to reactionsDeferred.await()
                        }
                        val data = posts.mapIndexed { index, postModel ->
                            PostContentModel(
                                postId = postModel.postId,
                                authorId = postModel.authorId,
                                imageUrl = postModel.imageUrl,
                                registerAt = postModel.registerAt,
                                description = postModel.description,
                                tags = tags[index].map { it.tagName },
                                isFollower = false,
                                reactions = reactions[index].mapNotNull { it.reaction },
                                myReaction = reactions[index].find { it.userId == currentUserId }?.reaction
                            )
                        }

                        FollowerThumbnail(
                            id = follower.followerId,
                            followerName = follower.followerName,
                            posts = data
                        )
                    }
                }.awaitAll()
            }

            LoadResult.Page(
                data = thumbnails,
                prevKey = if (key == null) null  else followers.firstOrNull()?.followerId,
                nextKey = if (followers.isEmpty()) null else followers.lastOrNull()?.followerId
            )
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