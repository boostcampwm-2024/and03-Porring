package com.kolown.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T>ListenableFuture<T>.getSuspendedResult(context: Context):T= suspendCoroutine{ continuation ->
    this.addListener({
        try {
            continuation.resume(get())
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    },ContextCompat.getMainExecutor(context))
}

fun CameraController.takePhoto(
    context: Context,
    onPhotoTaken: (Bitmap) -> Unit
) {
    this.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("testing", "Couldn't take photo: ", exception)
            }
        }
    )
}
