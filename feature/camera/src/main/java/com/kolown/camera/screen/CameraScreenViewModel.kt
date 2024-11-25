package com.kolown.camera.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.camera.core.internal.utils.ImageUtil.rotateBitmap
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
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
