package com.kolown.network

import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun FirebaseRemoteConfig.getTaskAsync(): Task<Boolean> = suspendCoroutine { continuation ->
    this.fetchAndActivate().addOnCompleteListener { task ->
        continuation.resume(task)
    }
}
