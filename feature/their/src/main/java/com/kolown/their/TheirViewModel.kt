package com.kolown.their

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TheirViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
) : ViewModel() {
    private val _followerName = MutableStateFlow("")
    val followerName = _followerName.asStateFlow()

    private var _firstPage = 0
    val firstPage get() = _firstPage

    private val reactionStateFlow = MutableStateFlow<Map<String, ReactionState>>(emptyMap())

    private val _userId = MutableStateFlow("")
    val galleryFlow = _userId.flatMapLatest { userId ->
        val pagingFlow = postRepository.getUserPosts(userId).cachedIn(viewModelScope)

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
        }.cachedIn(viewModelScope)

        combineFlow

    }.cachedIn(viewModelScope)

    fun setPage(page: Int) {
        _firstPage = page
    }

    fun setFollowerName(followerId: String) {
        _userId.update { followerId }
        followRepository.getFollowerName(followerId)
            .onEach { name -> _followerName.update {
                if(name == "") {
                    "Anonymous"
                } else {
                    name
                }
            } }
            .catch {
                _followerName.update { "Anonymous" }
            }
            .launchIn(viewModelScope)
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
}

data class ReactionState(
    val prev: Reactions? = null,
    val current: Reactions = Reactions.LOVE,
)