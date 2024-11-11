package com.kolown.camera

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AppDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
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
