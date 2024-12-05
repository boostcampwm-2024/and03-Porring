package com.kolown.data.repository

import com.kolown.datastore.AppDataSource
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {
    val cameraPermissionDinedCountFlow: Flow<Int>
    val cameraPermissionDinedStateFlow: Flow<Boolean>
    suspend fun increaseCameraPermissionDinedCount()
    suspend fun resetCameraPermissionDinedCount()
}

class AppDataRepositoryImpl(private val appDataSource: AppDataSource) : AppDataRepository {
    override val cameraPermissionDinedCountFlow = appDataSource.cameraPermissionDinedCountFlow
    override val cameraPermissionDinedStateFlow = appDataSource.cameraPermissionDinedFlow

    override suspend fun resetCameraPermissionDinedCount() {
        appDataSource.resetCameraPermissionDinedCount()
    }

    override suspend fun increaseCameraPermissionDinedCount() {
        appDataSource.increaseCameraPermissionDinedCount()
    }

}
