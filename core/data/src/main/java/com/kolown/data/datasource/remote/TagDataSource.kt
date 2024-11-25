package com.kolown.data.datasource.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kolown.data.remote.TagDto
import com.kolown.data.remote.toTagModel
import com.kolown.model.TagModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TagDataSource {
    suspend fun uploadPostTags(tagIds: List<String>, postId: String)
    suspend fun uploadTags(tags: List<String>): Result<List<String>>
    suspend fun getPostTag(postId: String): Result<List<TagModel>>
}

class TagDataSourceImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : TagDataSource {
    private val postTagCollection = firestore.collection("postTag")
    private val tagCollection = firestore.collection("tag")

    override suspend fun uploadPostTags(tagIds: List<String>, postId: String) {
        tagIds.forEach { tagId ->
            val uploadData = mapOf(
                "postId" to postId,
                "tagId" to tagId
            )

            postTagCollection
                .add(uploadData)
                .await()
                .let {
                    it.update("postTagId", "postTag-${it.id}")
                }
        }
    }

    override suspend fun uploadTags(tags: List<String>): Result<List<String>> {
        return runCatching {
            tags.map { tag ->
                // tag name이 존재하면 그 tag의 id를 반환
                if (tagCollection.contains("tagName", tag).getOrThrow()) {
                    tagCollection
                        .whereEqualTo("tagName", tag)
                        .get()
                        .await()
                        .first()
                        .data["tagId"].toString()
                } else {
                    // 존재하지 않으면 tag를 추가해서 id 반환
                    tagCollection
                        .add(mapOf("tagName" to tag))
                        .await()
                        .let {
                            it.update("tagId", "tag-${it.id}")
                            "tag-${it.id}"
                        }
                }
            }
        }
    }

    override suspend fun getPostTag(postId: String): Result<List<TagModel>> {
        return runCatching {
            val tagIds = postTagCollection
                .whereEqualTo("postId", postId)
                .get()
                .await()
                .map { it.data["tagId"].toString() }

            coroutineScope {
                tagIds.map { tagId ->
                    async {
                        tagCollection
                            .whereEqualTo("tagId", tagId)
                            .get()
                            .await()
                            .map { it.toObject(TagDto::class.java).toTagModel() }.first()
                    }
                }.awaitAll()
            }
        }
    }

    private suspend fun CollectionReference.contains(
        field: String,
        value: String,
    ): Result<Boolean> {
        return kotlin.runCatching {
            this
                .whereEqualTo(field, value)
                .get()
                .await()
                .isEmpty
                .not()
        }
    }
}