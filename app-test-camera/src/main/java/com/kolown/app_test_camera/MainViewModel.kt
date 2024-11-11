package com.kolown.app_test_camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AppDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appDataRepository: AppDataRepository) :
    ViewModel() {
    val cameraPermissionDinedStateFlow = appDataRepository.cameraPermissionDinedStateFlow

    fun increaseCameraPermissionDinedCount() {
        viewModelScope.launch {
            appDataRepository.increaseCameraPermissionDinedCount()
        }
    }

    fun resetCameraPermissionDinedCount() {
        viewModelScope.launch {
            appDataRepository.resetCameraPermissionDinedCount()
        }
    }
}
