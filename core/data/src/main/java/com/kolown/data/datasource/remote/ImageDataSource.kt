package com.kolown.data.datasource.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface ImageDataSource {
    suspend fun getImageUrl(authorId: String, fileUri: Uri): Result<String>
}

class ImageDataSourceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageDataSource {
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