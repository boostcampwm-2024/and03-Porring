package com.kolown.data.repository

import android.R.attr.path
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import javax.inject.Inject


interface ImageCacheRepository {
    suspend fun saveBitmapToCache(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 100
    ): Uri?
    suspend fun clearCacheFiles()
    suspend fun decodeSampledBitmapFromUri(uri: Uri): Bitmap?
}

class ImageCacheRepositoryImpl @Inject constructor(private val applicationContext: Context) :
    ImageCacheRepository {
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun saveBitmapToCache(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat,
        quality: Int
    ): Uri? {
        return try {
            val file = File(applicationContext.cacheDir, "photo_${System.currentTimeMillis()}.jpg")

            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(format, quality, outputStream)
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

        val rotatedUri = rotateImageAndReturnUri(uri) ?: return null

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        applicationContext.contentResolver.openInputStream(rotatedUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }
        options.inSampleSize = calculateInSampleSize(options)

        options.inJustDecodeBounds = false

        return applicationContext.contentResolver.openInputStream(rotatedUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }
    }

    private fun rotateImageAndReturnUri(uri: Uri): Uri? {
        var exif: ExifInterface? = null
        var outputUri: Uri? = null
        var rotatedBitmap: Bitmap? = null

        val inputStream = applicationContext.contentResolver.openInputStream(uri)

        inputStream?.use { stream ->
            try {
                exif = ExifInterface(stream)
            } catch (e: IOException) {
                Log.e("회전 에러", e.message.toString())
            }
        }

        applicationContext.contentResolver.openInputStream(uri)?.use { stream ->
            val originalBitmap = BitmapFactory.decodeStream(stream)
            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL

            rotatedBitmap = rotateBitmap(orientation, originalBitmap)
        }

        rotatedBitmap?.let {
            val file =
                File(applicationContext.cacheDir, "rotated_photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            outputUri = Uri.fromFile(file)
        }

        return outputUri
    }

    private fun rotateBitmap(orientation: Int, source: Bitmap): Bitmap {
        val angle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            ExifInterface.ORIENTATION_NORMAL -> 0f
            else -> 0f
        }
        val matrix = Matrix().apply {
            postRotate(angle)
        }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

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

