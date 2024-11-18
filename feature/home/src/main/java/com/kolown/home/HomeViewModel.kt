package com.kolown.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.FakeImageRepository
import com.kolown.data.repository.ImageRepository
import com.kolown.model.ImageItem
import com.kolown.model.Reactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @FakeImageRepository
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _mainFeedImageItems = MutableStateFlow<List<ImageItem>>(emptyList())
    val mainFeedImageItems = _mainFeedImageItems.asStateFlow()

    init {
        loadImageItem()
    }

    fun followUser(id: Long) {
        _mainFeedImageItems.update {
            it.map { imageItem ->
                if (imageItem.id == id) {
                    imageItem.copy(isFollowed = !imageItem.isFollowed)
                } else {
                    imageItem
                }
            }
        }
    }

    fun selectReaction(id: Long, reaction: Reactions) {
        _mainFeedImageItems.update {
            it.map { imageItem ->
                if (imageItem.id == id) {
                    imageItem.copy(reactions = reaction)
                } else {
                    imageItem
                }
            }
        }
    }

    fun loadImageItem() {
        viewModelScope.launch {
            imageRepository.getItems().collect {
                _mainFeedImageItems.value = it
            }
        }
    }

}