package com.kolown.their

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kolown.data.di.Fake
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.GalleryRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class TheirViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<UiState<Flow<PagingData<PostContentModel>>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _followerName = MutableStateFlow("Anonymous")
    val followerName = _followerName.asStateFlow()

    fun getFollowerGallery(followerId: String) {
        try {
            _uiState.value =
                UiState.Success(postRepository.getUserPosts(followerId).cachedIn(viewModelScope))
        } catch (e: Exception) {
            _uiState.value = UiState.Failure(e)
        }
    }

    fun setFollowerName(followerId: String) {
        followRepository.getFollowerName(followerId)
            .onEach { _followerName.value = it }
            .catch {
                Log.e("GetFollowerName", "Error: ${it.message}")
            }
            .launchIn(viewModelScope)
    }

}
