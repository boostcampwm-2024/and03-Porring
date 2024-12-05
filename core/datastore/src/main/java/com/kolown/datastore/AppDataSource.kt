package com.kolown.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.permissionDataStore: DataStore<Preferences> by preferencesDataStore(name = "permissions")

interface AppDataSource {
    val cameraPermissionDinedCountFlow: Flow<Int>
    val cameraPermissionDinedFlow: Flow<Boolean>
    suspend fun increaseCameraPermissionDinedCount()
    suspend fun resetCameraPermissionDinedCount()

}

class AppDataSourceImpl @Inject constructor(private val context: Context) : AppDataSource {

    private val cameraPermissionDinedCountKey = intPreferencesKey(CAMERA_PERMISSION_DINED_COUNT_KEY)
    override val cameraPermissionDinedCountFlow = context.permissionDataStore.data
        .map { preferences ->
            preferences[cameraPermissionDinedCountKey]
                ?: 0
        }

    override val cameraPermissionDinedFlow = cameraPermissionDinedCountFlow.map { it > 1 }

    override suspend fun resetCameraPermissionDinedCount() {
        context.permissionDataStore.edit { preferences ->
            preferences[cameraPermissionDinedCountKey] = 0
        }
    }

    override suspend fun increaseCameraPermissionDinedCount() {
        context.permissionDataStore.edit { preferences ->
            val currentCount = preferences[cameraPermissionDinedCountKey] ?: 0
            preferences[cameraPermissionDinedCountKey] = currentCount + 1
        }
    }


    companion object {
        private const val CAMERA_PERMISSION_DINED_COUNT_KEY = "CAMERA_PERMISSION_DINE_COUNT_KEY"
    }
}



