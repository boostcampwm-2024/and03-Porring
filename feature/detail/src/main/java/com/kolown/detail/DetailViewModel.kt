package com.kolown.detail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kolown.data.repository.FakeImageRepository
import com.kolown.data.repository.ImageRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.ImageItem
import com.kolown.model.PostContentModel
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository  : PostRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Flow<PagingData<PostContentModel>>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var _items =  emptyList<ImageItem>()

    init {
        getItem()
    }

    fun getItem() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val pagingData = postRepository.getRandomDetailPostList().cachedIn(viewModelScope)
                _uiState.value = UiState.Success(pagingData)
            } catch (e: Exception) {
                _uiState.value = UiState.Failure(e)
            }
        }
    }

    fun getPagingItem() : List<ImageItem> = _items
}