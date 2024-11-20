package com.kolown.upload

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.ImageCacheRepository
import com.kolown.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: ImageCacheRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _categoryItems = MutableStateFlow<List<String>>(emptyList())
    val categoryItems = _categoryItems.asStateFlow()

    private val webPUri = MutableStateFlow<Uri?>(null)

    fun changeDescription(description: String) {
        _description.value = description
    }

    fun addCategory() {
        _categoryItems.value += ""
    }

    fun changeCategoryName(index: Int, name: String) {
        val newList = _categoryItems.value.toMutableList()
        newList[index] = name
        _categoryItems.value = newList
    }

    fun removeCategory(category: String) {
        _categoryItems.value -= category
    }

    fun getUriWebP(uri: String) {
        viewModelScope.launch {
            var retries = 0
            val maxRetries = 3
            var bitmap: Bitmap? = null
            while (retries < maxRetries) {
                bitmap = repository.decodeSampledBitmapFromUri(Uri.parse(uri))
                if (bitmap != null) break
                retries++
            }
            bitmap?.let {
                webPUri.value = repository.saveBitmapToCache(it, Bitmap.CompressFormat.WEBP, 80)
            }
        }
    }

    fun uploadPost() {
        viewModelScope.launch {
            webPUri.value?.let {
                postRepository.uploadPost(
                    fileUri = it,
                    description = description.value,
                    tags = categoryItems.value
                )
            }

        }
    }

}