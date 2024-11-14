package com.kolown.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface ImageCacheRepository {
    suspend fun saveBitmapToCache(bitmap: Bitmap): Uri?
    suspend fun clearCacheFiles()
    suspend fun decodeSampledBitmapFromUri(uri: Uri): Bitmap?

}

class ImageCacheRepositoryImpl @Inject constructor(private val applicationContext: Context) :
    ImageCacheRepository {
    override suspend fun saveBitmapToCache(bitmap: Bitmap): Uri? {
        return try {
            val file = File(applicationContext.cacheDir, "photo_${System.currentTimeMillis()}.jpg")

            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)   // todo 상수 변환 필요
                }
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    }

    override suspend fun clearCacheFiles() {
        val cacheDir = applicationContext.cacheDir
        if (cacheDir.isDirectory) {
            cacheDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }

    override suspend fun decodeSampledBitmapFromUri(
        uri: Uri,
    ): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        applicationContext.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options)

        options.inJustDecodeBounds = false
        return applicationContext.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        // todo 상수 변환 필요(900, 720)

        if (height > 900 || width > 720) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= 900 && halfWidth / inSampleSize >= 720) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}

