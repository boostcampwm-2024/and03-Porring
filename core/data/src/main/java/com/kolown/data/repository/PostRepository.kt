package com.kolown.data.repository

import android.net.Uri
import android.util.Log
import com.kolown.data.datasource.remote.ImageDataSource
import com.kolown.data.datasource.remote.PostDataSource
import com.kolown.data.datasource.remote.ReactionDataSource
import com.kolown.data.datasource.remote.TagDatasource
import com.kolown.model.PostContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

interface PostRepository {
    suspend fun uploadPost(fileUri: Uri, description: String, tags: List<String>): Result<Unit>
    suspend fun getRandomPostList(count: Int): Result<List<PostContentModel>>
}

class PostRepositoryImpl @Inject constructor(
    private val imageDataSource: ImageDataSource,
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDatasource,
    private val reactionDataSource: ReactionDataSource
) : PostRepository {
    private val authorId = "user-1feIeEN3rMh4ZY7YpQKxDfnKGvi2"

    override suspend fun uploadPost(
        fileUri: Uri,
        description: String,
        tags: List<String>
    ): Result<Unit> {
        val time = System.currentTimeMillis()
        return runCatching {
            CoroutineScope(Dispatchers.IO).launch {
                // post Upload to Firestore & get postId
                val postIdDeferred = async {
                    postDataSource.uploadPost(authorId, description).getOrElse {
                        throw IOException("게시물 업로드 실패")
                    }
                }
                val tagIdsDeferred = async {
                    tagDataSource.uploadTags(tags).getOrElse {
                        throw IOException("태그 업로드 실패")
                    }
                }

                val (postId, tagIds) = postIdDeferred.await() to tagIdsDeferred.await()
                Log.d("UploadTime", "uploadSuccess: ${System.currentTimeMillis() - time}")

                // todo 실패했을때 처리 필요
                launch {
                    tagDataSource.uploadPostTags(tagIds, postId)
                    Log.d("UploadTime", "tagtime: ${System.currentTimeMillis() - time}")
                }

                launch {
                    updateImageUrl(postId, fileUri)
                    Log.d("UploadTime", "urltime: ${System.currentTimeMillis() - time}")
                }
            }
        }
    }

    override suspend fun getRandomPostList(count: Int): Result<List<PostContentModel>> {
        return runCatching {
            val mockAuthorId = "mock"

            val posts = postDataSource.getRandomPost(mockAuthorId, count).getOrElse {
                throw IOException("게시물 불러오기 실패")
            }
            val (tags, reactions) = coroutineScope {
                val tagsDeferred = async {
                    posts.map {
                        tagDataSource.getPostTag(it.postId).getOrElse {
                            throw IOException("태그 불러오기 실패")
                        }
                    }
                }
                val reactionsDeferred = async {
                    posts.map {
                        reactionDataSource.getReactionByPostId(it.postId).getOrElse {
                            throw IOException("리액션 불러오기 실패")
                        }
                    }
                }

                tagsDeferred.await() to reactionsDeferred.await()
            }

            posts.mapIndexed { index, postModel ->
                PostContentModel(
                    authorId = postModel.authorId,
                    imageUrl = postModel.imageUrl,
                    registerAt = postModel.registerAt,
                    description = postModel.description,
                    tags = tags[index].map { it.tagName },
                    isFollower = false,
                    reactions = reactions[index]
                )
            }
        }
    }

    private suspend fun updateImageUrl(postId: String, fileUri: Uri) {
        val documentId = postId.substringAfter("-")
        val imageUrl = imageDataSource.getImageUrl(authorId, fileUri).getOrElse {
            throw IOException("이미지 업로드 실패")
        }
        postDataSource.updateImageUrl(documentId, imageUrl)
    }

}