package com.kolown.their

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolown.data.di.Fake
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TheirViewModel @Inject constructor(
    @Fake
    private val galleryRepository: GalleryRepository,
    private val followRepository: FollowRepository
) : ViewModel() {
    val galleryFlow = galleryRepository.getGalleryThumbnailPagingFlow(1).cachedIn(viewModelScope)

    private val _followerName = MutableStateFlow("Anonymous")
    val followerName = _followerName.asStateFlow()

    fun setFollowerName(followerId: String) {
        followRepository.getFollowerName(followerId)
            .onEach { _followerName.value = it }
            .catch {
                Log.e("GetFollowerName", "Error: ${it.message}")
            }
            .launchIn(viewModelScope)
    }

}
