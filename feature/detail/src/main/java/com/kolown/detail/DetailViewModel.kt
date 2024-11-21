package com.kolown.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kolown.data.repository.PostRepository
import com.kolown.detail.navigation.PostType
import com.kolown.model.PostContentModel
import com.kolown.model.UiState
import com.kolown.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val typeMap = mapOf(
        typeOf<PostContentModel>() to PostType,
    )


    private val _uiState =
        MutableStateFlow<UiState<Flow<PagingData<PostContentModel>>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()


    private val post: PostContentModel =
        savedStateHandle.toRoute<AppRoute.Detail>(typeMap).postContentModel

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

    fun getPagingItem(): PostContentModel = post
}