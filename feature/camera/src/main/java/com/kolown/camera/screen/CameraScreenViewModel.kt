package com.kolown.camera.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AppDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel @Inject constructor(
    private val appDataRepository: AppDataRepository
) : ViewModel() {

    private val _cameraPermissionDinedCountFlow = appDataRepository.cameraPermissionDinedCountFlow


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
