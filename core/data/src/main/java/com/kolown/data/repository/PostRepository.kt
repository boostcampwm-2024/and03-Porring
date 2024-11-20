package com.kolown.data.repository

import android.net.Uri
import com.kolown.data.datasource.ImageDataSource
import com.kolown.data.datasource.PostDataSource
import com.kolown.data.datasource.TagDatasource
import java.io.IOException
import javax.inject.Inject

interface PostRepository {
    suspend fun uploadPost(fileUri: Uri, description: String, tags: List<String>): Result<Unit>
}

class PostRepositoryImpl @Inject constructor(
    private val imageDataSource: ImageDataSource,
    private val postDataSource: PostDataSource,
    private val tagDataSource: TagDatasource
) : PostRepository {
    private val authorId = "user-1feIeEN3rMh4ZY7YpQKxDfnKGvi2"

    override suspend fun uploadPost(
        fileUri: Uri,
        description: String,
        tags: List<String>
    ): Result<Unit> {
        return runCatching {
            // Image File Upload & get Image Uri
            val imageUri = imageDataSource.getImageUrl(authorId, fileUri).getOrElse {
                throw IOException("이미지 업로드 실패")
            }
            // post Upload to Firestore & get postId
            val postId = postDataSource.uploadPost(authorId, imageUri, description).getOrElse {
                throw IOException("게시물 업로드 실패")
            }
            tagDataSource.uploadTags(tags, postId)
        }
    }

}