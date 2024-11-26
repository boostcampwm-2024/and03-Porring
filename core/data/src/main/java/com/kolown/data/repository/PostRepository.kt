package com.kolown.data.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kolown.data.datasource.paging.RandomPagingDataSource
import com.kolown.data.datasource.paging.SearchPagingSource
import com.kolown.data.datasource.paging.UserPagingDataSource
import com.kolown.data.datasource.paging.UserPagingKey
import com.kolown.data.datasource.remote.AuthDataSource
import com.kolown.data.datasource.remote.FollowDataSource
import com.kolown.data.datasource.remote.ImageDataSource
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.TagDataSource
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

interface PostRepository {
    suspend fun uploadPost(fileUri: Uri, description: String, tags: List<String>): Result<Unit>
    fun getRandomPostList(count: Int): Flow<List<PostContentModel>>
    suspend fun getRandomDetailPostList(): Flow<PagingData<PostContentModel>>
    suspend fun reactPost(postId: String, reaction: Reactions): Result<Unit>
    suspend fun removePostReaction(postId: String): Result<Unit>
    fun getUserPosts(userId: String): Flow<PagingData<PostContentModel>>
    suspend fun getPostBySearch(tagId: String): Flow<PagingData<PostContentModel>>
}

class PostRepositoryImpl @Inject constructor(
    private val imageDataSource: ImageDataSource,
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDataSource,
    private val reactionDataSource: ReactionDataSource,
    private val randomPagingDataSource: RandomPagingDataSource,
    @Named("google") private val googleAuthDataSource: AuthDataSource,
    private val followDataSource: FollowDataSource,
    private val userPagingDataSource: UserPagingDataSource,
) : PostRepository {

    override fun getUserPosts(userId: String): Flow<PagingData<PostContentModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            initialKey = UserPagingKey(1, userId),
            pagingSourceFactory = { userPagingDataSource }
        ).flow
    }

    override suspend fun uploadPost(
        fileUri: Uri,
        description: String,
        tags: List<String>,
    ): Result<Unit> {
        return runCatching {
            coroutineScope {
                val authorId = googleAuthDataSource.getUserId()

                val postIdDeferred = async {
                    retryWithLimit {
                        postDataSource.uploadPost(authorId, description).getOrElse {
                            throw IOException("게시물 업로드 실패")
                        }
                    }.getOrThrow()
                }
                val tagIdsDeferred = async {
                    retryWithLimit {
                        tagDataSource.uploadTags(tags).getOrElse {
                            throw IOException("태그 업로드 실패")
                        }
                    }.getOrThrow()
                }

                val (postId, tagIds) = postIdDeferred.await() to tagIdsDeferred.await()

                launch {
                    retryWithLimit {
                        tagDataSource.uploadPostTags(tagIds, postId).getOrElse {
                            throw IOException("포스트 태그 업로드 실패")
                        }
                    }.getOrThrow()
                }

                launch {
                    retryWithLimit {
                        updateImageUrl(postId, fileUri).getOrElse {
                            throw IOException("이미지 uri 업로드 실패")
                        }
                    }.getOrThrow()
                }
            }
        }
    }

    override fun getRandomPostList(count: Int): Flow<List<PostContentModel>> = flow {
        val currentUserId = googleAuthDataSource.getUserId()

        val posts = postDataSource.getRandomPost(currentUserId, count).getOrElse {
            throw IOException("게시물 불러오기 실패")
        }
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
                        followDataSource.getIsFollower(
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

        val postContentModels = posts.mapIndexed { index, postModel ->
            PostContentModel(
                postId = postModel.postId,
                authorId = postModel.authorId,
                imageUrl = postModel.imageUrl,
                registerAt = postModel.registerAt,
                description = postModel.description,
                tags = tags[index].map { it.tagName },
                isFollower = isFollowers[index],
                reactions = reactions[index].mapNotNull { it.reaction },
                myReaction = reactions[index].find { it.userId == currentUserId }?.reaction
            )
        }
        emit(postContentModels)
    }.catch { e ->
        throw e
    }

    override suspend fun getRandomDetailPostList(): Flow<PagingData<PostContentModel>> {
        return Pager(
            config = PagingConfig(pageSize = DETAIL_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = { randomPagingDataSource }
        ).flow
    }

    override suspend fun reactPost(postId: String, reaction: Reactions): Result<Unit> {
        return kotlin.runCatching {
            val currentUserId = googleAuthDataSource.getUserId()

            reactionDataSource.updatePostReaction(
                userId = currentUserId,
                postId = postId,
                reaction = reaction
            )
        }
    }

    override suspend fun removePostReaction(postId: String): Result<Unit> {
        return kotlin.runCatching {
            val currentUserId = googleAuthDataSource.getUserId()

            reactionDataSource.removePostReaction(
                userId = currentUserId,
                postId = postId,
            )
        }
    }

    override suspend fun getPostBySearch(tagId: String): Flow<PagingData<PostContentModel>> {
        return Pager(
            config = PagingConfig(pageSize = SEARCH_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = {
                SearchPagingSource(
                    postDataSource = postDataSource, reactionDataSource = reactionDataSource,
                    tagId = tagId, tagDataSource = tagDataSource
                )
            }
        ).flow
    }

    private suspend fun updateImageUrl(postId: String, fileUri: Uri): Result<Unit> {
        return runCatching {
            val authorId = googleAuthDataSource.getUserId()

            val documentId = postId.substringAfter("-")
            val imageUrl = imageDataSource.getImageUrl(authorId, fileUri).getOrElse {
                throw IOException("이미지 업로드 실패")
            }
            postDataSource.updateImageUrl(documentId, imageUrl)
        }
    }

    private suspend fun <T> retryWithLimit(
        maxAttempts: Int = 3,
        delayMillis: Long = 1000,
        block: suspend () -> T
    ): Result<T> {
        repeat(maxAttempts - 1) { attempt ->
            try {
                return Result.success(block())
            } catch (e: Exception) {
                if(attempt < maxAttempts - 1) {
                    delay(delayMillis)
                    Log.e("retryWithLimit", "retryWithLimit: attempt $e")
                }
            }
        }
        return runCatching { block() }
    }

    companion object {
        const val DETAIL_PER_PAGE = 2
        const val SEARCH_PER_PAGE = 5
    }
}
