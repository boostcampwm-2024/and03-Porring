package com.kolown.data.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.model.PostContentModel
import com.kolown.model.PostModel
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
    private val reactionDataSource: ReactionDataSource,
    private val userId: String
) : PagingSource<UserPagingKey, PostContentModel>() {
    override fun getRefreshKey(state: PagingState<UserPagingKey, PostContentModel>): UserPagingKey? {
        return state.anchorPosition?.let { position ->
            val closestPage = state.closestPageToPosition(position)
            val page = closestPage?.prevKey?.page?.plus(1)
                ?: closestPage?.nextKey?.page?.minus(1)
            postDataSource.resetLastVisible()
            UserPagingKey(page ?: 0, userId)
        }
    }

    override suspend fun load(params: LoadParams<UserPagingKey>): LoadResult<UserPagingKey, PostContentModel> {
        val page = params.key?.page ?: 0

        val posts = getPosts(params).getOrElse {
            return LoadResult.Error(it)
        }
        val data = getData(posts).getOrElse {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = data,
            prevKey = if (page == 0) null else UserPagingKey(page - 1, userId),
            nextKey = if (data.isEmpty()) null else UserPagingKey(page + 1, userId)
        )
    }

    private suspend fun getPosts(params: LoadParams<UserPagingKey>): Result<List<PostModel>> {
        return postDataSource.getUserPost(userId, params.loadSize.toLong())
    }

    private suspend fun getData(posts: List<PostModel>): Result<List<PostContentModel>> {
        return runCatching {
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

            posts.mapIndexed { index, postModel ->
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
            }
        }
    }

    class Factory @Inject constructor(
        private val postDataSource: PostDataSource,
        private val tagDataSource: TagDataSource,
        private val reactionDataSource: ReactionDataSource
    ) {
        fun create(userId: String): UserPagingDataSource {
            return UserPagingDataSource(postDataSource, tagDataSource, reactionDataSource, userId)
        }
    }

}