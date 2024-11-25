package com.kolown.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<PostContentModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentItems = listOf<PostContentModel>()

    init {
        loadImageItem()
    }

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
        postRepository.getRandomPostList(10)
            .onStart { _uiState.update { UiState.Loading } }
            .map { items ->
                currentItems = items
                UiState.Success(items)
            }
            .catch { e -> _uiState.update { UiState.Failure(e) } }
            .onEach { newState -> _uiState.update { newState } }
            .launchIn(viewModelScope)
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
}