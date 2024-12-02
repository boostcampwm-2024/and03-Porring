package com.kolown.follower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kolown.data.repository.FollowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val followerRepository: FollowRepository
) : ViewModel() {
    private val trigger = MutableStateFlow(0)
    val followerItems = trigger.flatMapLatest { key ->
        followerRepository.getFollowerDataSourcePagingFlow()
    }

    fun resetGalleryFlow() {
        trigger.value++
    }

}