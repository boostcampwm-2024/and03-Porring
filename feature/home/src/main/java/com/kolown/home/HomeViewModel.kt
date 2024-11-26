package com.kolown.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<PostContentModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentItems = listOf<PostContentModel>()

    fun followUser(id: String, name: String) {
        updateFollow(id)
        followRepository.followUser(id, name)
            .catch { Log.e("FollowUpload", "viewModel: $it") }
            .launchIn(viewModelScope)
    }

    fun unFollowUser(id: String) {
        viewModelScope.launch {
            updateFollow(id)
            followRepository.unFollowUser(id)
                .catch { Log.e("UnFollowUpload", "viewModel: $it") }
                .launchIn(viewModelScope)
        }
    }

    fun selectReaction(item: PostContentModel, reaction: Reactions) {
        if (item.myReaction == reaction) {
            viewModelScope.launch { postRepository.removePostReaction(item.postId) }
        } else {
            viewModelScope.launch {
                postRepository.reactPost(
                    postId = item.postId, reaction = reaction
                )
            }
        }
    }

    private fun updateFollow(id: String) {
        currentItems = currentItems.map {
            if (it.authorId == id) {
                it.copy(isFollower = !it.isFollower)
            } else {
                it
            }
        }

        _uiState.update { UiState.Success(currentItems) }
    }

    fun updateItems(result: Result<List<PostContentModel>>) {
        result.onSuccess { items ->
            _uiState.update { UiState.Success(items) }
        }.onFailure { e ->
            _uiState.update { UiState.Failure(e) }
        }

        _uiState.update { UiState.Success(currentItems) }
    }
}