package com.kolown.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kolown.data.datasource.AuthDataSource
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

class RandomPagingDataSource @Inject constructor(
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDataSource,
    private val reactionDataSource: ReactionDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
) : PagingSource<Long, PostContentModel>() {
    private val randomSeed = (0..Long.MAX_VALUE).random()

    override fun getRefreshKey(state: PagingState<Long, PostContentModel>): Long? {
        return state.anchorPosition?.toLong()
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, PostContentModel> {
        val authorId = googleAuthDataSource.getUserId()
        val page = params.key ?: randomSeed
        val posts =
            postDataSource.getRandomPost(authorId, page, params.loadSize.toLong()).getOrElse {
                throw IOException("랜덤 게시글 불러오기 실패")
            }
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
                        reactionDataSource.getReactionByPostId(it.postId).getOrElse {
                            throw IOException("리액션 불러오기 실패")
                        }
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
                    isFollower = false,
                    reactions = reactions[index].mapNotNull { it.reaction }
                )
            },
            prevKey = if (page == randomSeed) null else posts.lastOrNull()?.random,
            nextKey = if (posts.isEmpty()) null else posts.last().random + 1
        )
    }
}