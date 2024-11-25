package com.kolown.their

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolown.data.di.Fake
import com.kolown.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TheirViewModel @Inject constructor(
    @Fake
    private val galleryRepository: GalleryRepository
) : ViewModel() {
    val galleryFlow = galleryRepository.getGalleryThumbnailPagingFlow(1).cachedIn(viewModelScope)

    private val _followerName = MutableStateFlow("")
    val followerName = _followerName.asStateFlow()

    fun setFollowerName(followerId: String) {
        _followerName.value = "유저"
    }

}
