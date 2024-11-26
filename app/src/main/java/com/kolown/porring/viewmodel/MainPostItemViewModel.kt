package com.kolown.porring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainPostItemViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _mainItems = MutableStateFlow<Flow<List<PostContentModel>>>(flow { })
    val mainItems = _mainItems.asStateFlow()

    private val _detailFirstItem =
        MutableStateFlow(PostContentModel("", "", "", "", "", emptyList(), false, emptyList()))
    val detailFirstItem = _detailFirstItem.asStateFlow()

    private var currentItems: List<PostContentModel> = emptyList()

    init {
        loadImageItem()
    }

    private fun loadImageItem() {
        postRepository.getRandomPostList(10).let { flow ->
            _mainItems.update { flow }
            flow.onEach { currentItems = it }.launchIn(viewModelScope)
        }
    }

    fun selectReaction(new: PostContentModel, reaction: Reactions) {
        currentItems = currentItems.map { prev ->
            prev.takeIf { prev.postId == new.postId }?.let {
                if (prev.myReaction == null) {
                    prev.copy(
                        reactions = prev.reactions + reaction, myReaction = reaction
                    )
                } else {
                    if (prev.myReaction == reaction) {
                        prev.copy(
                            reactions = prev.reactions - reaction, myReaction = null
                        )
                    } else {
                        prev.copy(
                            reactions = prev.reactions - prev.myReaction!! + reaction,
                            myReaction = reaction
                        )
                    }
                }
            } ?: prev
        }

        _mainItems.update { flow { emit(currentItems) } }
        currentItems.find { new.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }

    fun updateFollow(id: String) {
        currentItems = currentItems.map {
            if (it.authorId == id) {
                it.copy(isFollower = !it.isFollower)
            } else {
                it
            }
        }

        _mainItems.update { flow { emit(currentItems) } }
    }

    fun fetchDetailFirst(item: PostContentModel) {
        currentItems.find { item.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }
}