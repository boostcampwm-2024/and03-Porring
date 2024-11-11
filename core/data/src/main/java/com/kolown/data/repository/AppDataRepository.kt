package com.kolown.data.repository

import com.kolown.data.datasource.AppDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

interface AppDataRepository {
    val cameraPermissionDinedCountFlow: Flow<Int>
    val cameraPermissionDinedStateFlow: Flow<Boolean>
    suspend fun increaseCameraPermissionDinedCount()
    suspend fun resetCameraPermissionDinedCount()
}

class AppDataRepositoryImpl(private val appDataSource: AppDataSource) : AppDataRepository {
    override val cameraPermissionDinedCountFlow = appDataSource.cameraPermissionDinedCountFlow
    override val cameraPermissionDinedStateFlow = appDataSource.cameraPermissionDinedStateFlow

    override suspend fun resetCameraPermissionDinedCount() {
        appDataSource.resetCameraPermissionDinedCount()
    }

    override suspend fun increaseCameraPermissionDinedCount() {
        appDataSource.increaseCameraPermissionDinedCount()
    }

}
