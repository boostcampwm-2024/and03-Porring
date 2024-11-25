package com.kolown.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<PostContentModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentItems = listOf<PostContentModel>()

    init {
        loadImageItem()
    }

    //    fun followUser(id: String) {
//        if (_uiState.value is UiState.Success) {
//            val items = (_uiState.value as UiState.Success).data
//            _uiState.value = UiState.Success(
//                items.map { imageItem ->
//                    if (imageItem.id == id) {
//                        imageItem.copy(isFollowed = !imageItem.isFollowed)
//                    } else {
//                        imageItem
//                    }
//                }
//            )
//        }
//    }
//

    fun selectReaction(postId: String, reaction: Reactions) {
        val currentReaction = currentItems.find { it.postId == postId }?.myReaction

        if (currentReaction == reaction) {
            currentItems = currentItems.map { item ->
                item.takeIf { it.postId == postId }?.copy(
                    reactions = item.reactions - reaction,
                    myReaction = null
                ) ?: item
            }
            viewModelScope.launch { postRepository.removePostReaction(postId) }
        } else {
            currentItems = currentItems.map { item ->
                item.takeIf { it.postId == postId }?.copy(
                    reactions = if (item.myReaction != null) item.reactions - item.myReaction!! + reaction else item.reactions + reaction,
                    myReaction = reaction
                ) ?: item
            }
            viewModelScope.launch {
                postRepository.reactPost(
                    postId = postId, reaction = reaction
                )
            }
        }

        _uiState.update { UiState.Success(currentItems) }
    }

    private fun loadImageItem() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                postRepository.getRandomPostList(10).onSuccess { items ->
                    currentItems = items
                    _uiState.value = UiState.Success(currentItems.toList())
                }.onFailure { e ->
                    // 실패하면 다시 불러오는 로직 실행
                    _uiState.value = UiState.Failure(e)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Failure(e)
            }
        }
    }
}