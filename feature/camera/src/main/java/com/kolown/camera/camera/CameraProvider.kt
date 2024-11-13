package com.kolown.camera.camera

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun ListenableFuture<ProcessCameraProvider>.getCameraProvider(context: Context): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        this.addListener({
            continuation.resume(this.get())
        }, ContextCompat.getMainExecutor(context))
    }
