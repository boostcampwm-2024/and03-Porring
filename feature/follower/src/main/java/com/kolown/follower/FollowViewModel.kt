package com.kolown.follower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kolown.data.repository.FollowRepository
import com.kolown.model.FollowerThumbnail
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(
    private val followerRepository: FollowRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Flow<PagingData<FollowerThumbnail>>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getItem()
    }

    private fun getItem() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val follows =
                    followerRepository.getFollowerDataSourcePagingFlow().cachedIn(viewModelScope)
                _uiState.value = UiState.Success(follows)
            } catch (e:Exception) {
                _uiState.value = UiState.Failure(e)
            }
        }
    }


}