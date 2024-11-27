package com.kolown.my

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.kolown.data.di.Fake
import com.kolown.data.repository.GalleryRepository
import com.kolown.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    val galleryFlow = postRepository.getUserPosts().cachedIn(viewModelScope)

    fun deletePost(postId: String) {
        Log.e("GalleryItem", "postId: $postId")
        viewModelScope.launch {
            postRepository.deletePost(postId)
                .onEach {
                    Log.e("GalleryItem", "deletePost: $it")
                }
                .catch {
                    Log.e("GalleryItem", "deletePost error: $it")
                }
                .launchIn(viewModelScope)
        }
    }
}
