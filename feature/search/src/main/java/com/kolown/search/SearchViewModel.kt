package com.kolown.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kolown.data.repository.FollowRepository
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.TagRepository
import com.kolown.data.repository.UserRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _tag = MutableStateFlow<Tag?>(null)
    val tag = _tag.asStateFlow()

    private var _firstPage = 0
    val firstPage get() = _firstPage


    private val reactionStateFlow = MutableStateFlow<Map<String, ReactionState>>(emptyMap())

    private val _followSharedFlow = MutableSharedFlow<Pair<String, Boolean>>(0)
    val followState = _followSharedFlow.asSharedFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult = _searchQuery
        .debounce(SEARCH_DEBOUNCE_TIME_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                tagRepository.getTagBySearch(query)
            }
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val resultPostList = _tag
        .filter {
        it != null
    }.flatMapLatest { tag ->
        tag?.let {

            val pagingFlow = postRepository.getPostBySearch(tag.id)
                .onStart {
                    emit(PagingData.empty())
                }

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

            combineFlow

        } ?: flow { emit(PagingData.empty()) }
    }.cachedIn(viewModelScope)


    fun setSearchQuery(searchText: String) {
        _searchQuery.value = searchText
    }

    fun setTag(tag: Tag) {
        _tag.value = tag
    }

    fun setPage(page: Int) {
        _firstPage = page
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
            _followSharedFlow.emit(Pair(id, true))
        }
    }

    fun unFollowUser(id: String) {
        viewModelScope.launch {
            followRepository.unFollowUser(id)
                .catch { Log.e("UnFollowUpload", "viewModel: $it") }
                .launchIn(viewModelScope)
            _followSharedFlow.emit(Pair(id, false))
        }
    }

    fun checkPostIsMine(authorId: String) = userRepository.checkUserId(authorId)


    companion object {
        const val SEARCH_DEBOUNCE_TIME_MILLIS = 300L
    }
}

data class ReactionState(
    val prev: Reactions? = null,
    val current: Reactions = Reactions.LOVE,
)