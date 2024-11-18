package com.kolown.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.FakeImageRepository
import com.kolown.data.repository.ImageRepository
import com.kolown.model.ImageItem
import com.kolown.model.Reactions
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @FakeImageRepository
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<ImageItem>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadImageItem()
    }

    fun followUser(id: Long) {
        if (_uiState.value is UiState.Success) {
            val items = (_uiState.value as UiState.Success).data
            _uiState.value = UiState.Success(
                items.map { imageItem ->
                    if (imageItem.id == id) {
                        imageItem.copy(isFollowed = !imageItem.isFollowed)
                    } else {
                        imageItem
                    }
                }
            )
        }
    }

    fun selectReaction(id: Long, reaction: Reactions) {
        if (_uiState.value is UiState.Success) {
            val items = (_uiState.value as UiState.Success).data
            _uiState.value = UiState.Success(
                items.map { imageItem ->
                    if (imageItem.id == id) {
                        imageItem.copy(reactions = reaction)
                    } else {
                        imageItem
                    }
                }
            )
        }
    }

    private fun loadImageItem() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                imageRepository.getItems()
                    .catch { e ->
                        _uiState.value = UiState.Failure(e)
                    }
                    .collect { items ->
                        _uiState.value = UiState.Success(items)
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Failure(e)
            }
        }
    }

}