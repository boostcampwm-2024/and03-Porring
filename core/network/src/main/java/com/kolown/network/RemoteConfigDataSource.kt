package com.kolown.network

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class RemoteConfigDataSource {
    private val remoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 1
    }

    init {
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    suspend fun getVersionName(): String? {
        val task = remoteConfig.getTaskAsync()
        if (!task.isSuccessful)
            return null
        task.await()
        return remoteConfig.getString("version_name")
    }
}
