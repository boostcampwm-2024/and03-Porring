package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.FollowDataSource
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.model.PostContentModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


class SearchPagingSource(
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDataSource,
    private val reactionDataSource: ReactionDataSource,
    private val followerDataSource: FollowDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
    val tagId: String
) : PagingSource<String, PostContentModel>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PostContentModel> {
        return try {
            val currentUserId = googleAuthDataSource.getUserId()
            val postIds =
                tagDataSource.getPostTagByTagId(tagId).getOrElse { throw Exception("포스트 불러오기 실패") }
            val page = params.key
            val posts = postDataSource.getPostBySearch(
                currentUserId = currentUserId,
                postIds = postIds,
                key = page,
                perPage = params.loadSize.toLong()
            ).getOrElse { throw Exception("포스트 불러오기 실패") }


            val (tags, reactions, isFollowers) = coroutineScope {
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
                            reactionDataSource.getReactionByPostId(it.postId).getOrElse {
                                throw IOException("리액션 불러오기 실패")
                            }
                        }
                    }.awaitAll()
                }
                val followDeferred = async {
                    posts.map {
                        async {
                            followerDataSource.getIsFollower(
                                userId = currentUserId,
                                followerId = it.authorId
                            ).getOrElse {
                                throw IOException("팔로우 확인 실패")
                            }
                        }
                    }.awaitAll()
                }

                Triple(tagsDeferred.await(), reactionsDeferred.await(), followDeferred.await())
            }
            val lastPostRegisteredAt = posts.lastOrNull()?.postId

            LoadResult.Page(
                data = posts.mapIndexed { index, postModel ->
                    PostContentModel(
                        postId = postModel.postId,
                        authorId = postModel.authorId,
                        imageUrl = postModel.imageUrl,
                        registerAt = postModel.registerAt,
                        description = postModel.description,
                        tags = tags[index].map { it.tagName },
                        isFollower = isFollowers[index],
                        reactions = reactions[index].mapNotNull { it.reaction },
                        myReaction = reactions[index].find { it.userId == currentUserId }?. reaction
                    )
                },
                prevKey = if (page == null) null else posts.firstOrNull()?.postId,
                nextKey = lastPostRegisteredAt
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<String, PostContentModel>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}