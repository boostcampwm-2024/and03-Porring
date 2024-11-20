package com.kolown.data.datasource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface TagDatasource {
    suspend fun uploadTags(tags: List<String>, postId: String)
}

class TagDataSourceImpl @Inject constructor() : TagDatasource {
    private val postTagCollection = Firebase.firestore.collection("postTag")
    private val tagCollection = Firebase.firestore.collection("tag")

    override suspend fun uploadTags(tags: List<String>, postId: String) {
        tags.toTagIds().forEach { tagId ->
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

    private suspend fun List<String>.toTagIds(): List<String> {
        return this.map { tag ->
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