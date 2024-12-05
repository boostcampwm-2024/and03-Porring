package com.kolown.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _firstPage = 0
    val firstPage get() = _firstPage

    private val currentUserId = MutableStateFlow("")

    private val _isDeleteSuccess = MutableStateFlow(false)
    val isDeleteSuccess = _isDeleteSuccess.asStateFlow()

    val galleryFlow = currentUserId.flatMapLatest {
        postRepository.getUserPosts().cachedIn(viewModelScope)
    }

    init {
        setUserId()
    }

    fun setPage(page: Int) {
        _firstPage = page
    }

    fun setUserId() {
        val newId = userRepository.getUserData().getOrThrow()
        if (currentUserId.value != newId) {
            currentUserId.update { newId }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
                .onEach {
                    _isDeleteSuccess.update { !it }
                }
                .launchIn(viewModelScope)
        }
    }
}
