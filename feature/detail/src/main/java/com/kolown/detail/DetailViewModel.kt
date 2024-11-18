package com.kolown.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.FakeImageRepository
import com.kolown.data.repository.ImageRepository
import com.kolown.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @FakeImageRepository
    private val imageRepository  : ImageRepository
): ViewModel() {

    private var _imageItems: MutableStateFlow<List<ImageItem>> = MutableStateFlow(emptyList())
    val imageItems: StateFlow<List<ImageItem>> = _imageItems.asStateFlow()

    fun getItem(currentPage: Int) {
        imageRepository.getItemByPage(currentPage).onEach {
            _imageItems.value += it
        }.launchIn(viewModelScope)
    }
}