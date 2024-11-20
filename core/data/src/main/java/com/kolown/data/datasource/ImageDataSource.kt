package com.kolown.data.datasource

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface ImageDataSource {
    suspend fun getImageUrl(authorId: String, fileUri: Uri): Result<String>
}

class ImageDataSourceImpl : ImageDataSource {
    private val storage by lazy { Firebase.storage }

    override suspend fun getImageUrl(authorId: String, fileUri: Uri): Result<String> {
        return uploadImage(authorId.toRefName(), fileUri)
    }

    private suspend fun uploadImage(path: String, uri: Uri): Result<String> {
        return kotlin.runCatching {
            storage.reference.child(path).let { imgRef ->
                imgRef.putFile(uri).await()
                imgRef.downloadUrl.await().toString()
            }
        }
    }

    private fun String.toRefName(): String {
        val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

        return (this + timeStamp).toHash()
    }

    private fun String.toHash(): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(this.toByteArray())

        return hash.joinToString("") { "%02x".format(it) }
    }

}