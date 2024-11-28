package com.kolown.follower

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kolown.data.repository.FollowRepository
import com.kolown.model.FollowerThumbnail
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val followerRepository: FollowRepository
) : ViewModel() {

    val followerItems = followerRepository.getFollowerDataSourcePagingFlow().cachedIn(viewModelScope)


}