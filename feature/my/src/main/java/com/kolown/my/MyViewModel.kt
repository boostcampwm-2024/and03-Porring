package com.kolown.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.kolown.data.di.Fake
import com.kolown.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    @Fake
    private val galleryRepository: GalleryRepository
) : ViewModel() {
    val galleryFlow = galleryRepository.getGalleryThumbnailPagingFlow(1).cachedIn(viewModelScope)

}
