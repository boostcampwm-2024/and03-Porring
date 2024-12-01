package com.kolown.detail

import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<UiState<Flow<PagingData<PostContentModel>>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val reactionStateFlow = MutableStateFlow<Map<String, ReactionState>>(emptyMap())

    private val _followSharedFlow = MutableSharedFlow<Pair<String,Boolean>>(0)
    val followState = _followSharedFlow.asSharedFlow()

    private var _currentPage = 0
    val currentPage get() = _currentPage


    init {
        getItem()
    }

    private fun getItem() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val pagingFlow = postRepository.getRandomDetailPostList().cachedIn(viewModelScope)

                val combineFlow = combine(
                    pagingFlow, reactionStateFlow
                ) { paging, reaction ->
                    paging.map { item ->
                        reaction[item.postId]?.let { reactionState ->
                            val (myReaction, reactions) = if (reactionState.prev == null) {
                                reactionState.current to item.reactions + reactionState.current
                            } else {
                                if (reactionState.prev == reactionState.current) {
                                    null to item.reactions - reactionState.current
                                } else {
                                    reactionState.current to item.reactions + reactionState.current - reactionState.prev
                                }
                            }
                            item.copy(reactions = reactions, myReaction = myReaction)
                        } ?: item.copy()
                    }
                }

                _uiState.value = UiState.Success(combineFlow)

            } catch (e: Exception) {
                _uiState.value = UiState.Failure(e)
            }
        }
    }

    fun selectReaction(imageItem: PostContentModel, reaction: Reactions) {
        val currentReaction = imageItem.myReaction

        viewModelScope.launch {
            if (currentReaction == reaction) {
                postRepository.removePostReaction(imageItem.postId)
            } else {
                postRepository.reactPost(
                    postId = imageItem.postId, reaction = reaction
                )
            }
        }
        updateReactionState(imageItem.postId, currentReaction, reaction)
    }

    private fun updateReactionState(
        postId: String,
        prevReaction: Reactions?,
        currentReaction: Reactions,
    ) {
        reactionStateFlow.update { reactionState ->
            val newState = reactionState.toMutableMap()
            newState[postId] = ReactionState(prev = prevReaction, current = currentReaction)
            newState
        }
    }

    fun followUser(id: String, name: String) {
        viewModelScope.launch {
            followRepository.followUser(id, name)
                .catch { Log.e("FollowUpload", "viewModel: $it") }
                .launchIn(viewModelScope)
            _followSharedFlow.emit(Pair(id,true))
        }
    }

    fun unFollowUser(id: String) {
        viewModelScope.launch {
            followRepository.unFollowUser(id)
                .catch { Log.e("UnFollowUpload", "viewModel: $it") }
                .launchIn(viewModelScope)
            _followSharedFlow.emit(Pair(id,false))
        }
    }


    fun updatePage(page: Int) {
        _currentPage = page
    }


}

data class ReactionState(
    val prev: Reactions? = null,
    val current: Reactions = Reactions.LOVE,
)