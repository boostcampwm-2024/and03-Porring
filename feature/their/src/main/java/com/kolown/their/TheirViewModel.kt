package com.kolown.their

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TheirViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
) : ViewModel() {
    private val _followerName = MutableStateFlow("")
    val followerName = _followerName.asStateFlow()

    private val _userId = MutableStateFlow("")
    val galleryFlow = _userId.flatMapLatest { userId ->
        postRepository.getUserPosts(userId)
    }.cachedIn(viewModelScope)

    fun setFollowerName(followerId: String) {
        if(followerId.isBlank()) {
            _followerName.update { "Anonymous" }
            return
        }

        _userId.update { followerId }
        followRepository.getFollowerName(followerId)
            .onEach { name -> _followerName.update { name } }
            .catch {
                Log.e("GetFollowerName", "Error: ${it.message}")
            }
            .launchIn(viewModelScope)
    }
}
