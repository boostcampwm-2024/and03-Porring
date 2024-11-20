package com.kolown.camera.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AppDataRepository
import com.kolown.data.repository.ImageCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel @Inject constructor(
    private val imageCacheRepository: ImageCacheRepository,
) : ViewModel() {

    private val _uri = MutableStateFlow<Uri?>(null)
    val uri: StateFlow<Uri?> = _uri.asStateFlow()

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun saveBitmapToCache(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            _uri.value = imageCacheRepository.saveBitmapToCache(bitmap)
        }
    }

}
