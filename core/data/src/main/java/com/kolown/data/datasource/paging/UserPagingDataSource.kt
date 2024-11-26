package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.model.PostContentModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

data class UserPagingKey(
    val page: Int,
    val userId: String
)

class UserPagingDataSource @Inject constructor(
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDataSource,
    private val reactionDataSource: ReactionDataSource
) : PagingSource<UserPagingKey, PostContentModel>() {
    override fun getRefreshKey(state: PagingState<UserPagingKey, PostContentModel>): UserPagingKey {
        return UserPagingKey(state.anchorPosition ?: 0, "")
    }

    override suspend fun load(params: LoadParams<UserPagingKey>): LoadResult<UserPagingKey, PostContentModel> {
        val page = params.key?.page ?: 1
        val userId = params.key?.userId ?: ""
        val posts = postDataSource.getUserPost(userId, params.loadSize.toLong()).getOrThrow()
        val (tags, reactions) = coroutineScope {
            val tagsDeferred =
                async {
                    posts.map {
                        async {
                            tagDataSource.getPostTag(it.postId).getOrThrow()
                        }
                    }.awaitAll()
                }
            val reactionsDeferred = async {
                posts.map {
                    async {
                        reactionDataSource.getReactionByPostId(it.postId).getOrThrow()
                    }
                }.awaitAll()
            }
            tagsDeferred.await() to reactionsDeferred.await()
        }

        return LoadResult.Page(
            data = posts.mapIndexed { index, postModel ->
                PostContentModel(
                    postId = postModel.postId,
                    authorId = postModel.authorId,
                    imageUrl = postModel.imageUrl,
                    registerAt = postModel.registerAt,
                    description = postModel.description,
                    tags = tags[index].map { it.tagName },
                    isFollower = true,
                    reactions = reactions[index].mapNotNull { it.reaction },
                )
            },
            prevKey = if (page == 1) null else UserPagingKey(page - 1, userId),
            nextKey = if (posts.isEmpty()) null else UserPagingKey(page + 1, userId)
        )
    }
}