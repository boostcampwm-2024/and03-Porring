package com.kolown.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kolown.data.repository.PostRepository
import com.kolown.detail.navigation.PostType
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UiState
import com.kolown.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val typeMap = mapOf(
        typeOf<PostContentModel>() to PostType,
    )

    private val _uiState =
        MutableStateFlow<UiState<Flow<PagingData<PostContentModel>>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val post: PostContentModel =
        savedStateHandle.toRoute<AppRoute.Detail>(typeMap).postContentModel

    private val reactionStateFlow = MutableStateFlow<Map<String, ReactionState>>(emptyMap())

    init {
        getItem()
    }

    fun getItem() {
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

    fun getPagingItem(): PostContentModel = post

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
}

data class ReactionState(
    val prev: Reactions? = null,
    val current: Reactions = Reactions.LOVE,
)