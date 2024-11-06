package com.kolown.camera

import java.util.concurrent.Future
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


suspend fun <T> Future<T>.await(): T = suspendCoroutine { continuation ->
    try {
        val result = this.get()
        continuation.resume(result)
    } catch (e: Exception) {
        continuation.resumeWithException(e)
    }
}
