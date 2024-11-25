package com.kolown.follower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolown.data.repository.FollowerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val followerRepository: FollowerRepository
) : ViewModel() {
    val galleryFlow = followerRepository.getFollowerDataSourcePagingFlow(1).cachedIn(viewModelScope)
}