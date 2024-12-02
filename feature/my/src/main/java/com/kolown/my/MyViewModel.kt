package com.kolown.my

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.kolown.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val deletedPostIds = MutableStateFlow<Set<String>>(emptySet())
    private val trigger = MutableStateFlow(0)

    val galleryFlow = trigger.flatMapLatest { key ->
        combine(
            postRepository.getUserPosts().cachedIn(viewModelScope),
            deletedPostIds
        ) { pagingData, deletedIds ->
            pagingData.filter { post -> post.postId !in deletedIds }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
                .onEach {
                    deletedPostIds.update { deletedPostIds.value + postId }
                }
                .catch {
                    Log.e("GalleryItem", "deletePost error: $it")
                }
                .launchIn(viewModelScope)
        }
    }

    fun resetGalleryFlow() {
        deletedPostIds.value = emptySet()
        trigger.value++
        val test = postRepository.getUserPosts().cachedIn(viewModelScope)
    }
}
