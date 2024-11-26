package com.kolown.porring.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.PostRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPostItemViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _mainItems =
        MutableStateFlow<Result<List<PostContentModel>>>(Result.success(emptyList()))
    val mainItems = _mainItems.asStateFlow()

    private val _detailFirstItem =
        MutableStateFlow(PostContentModel("", "", "", "", "", emptyList(), false, emptyList()))
    val detailFirstItem = _detailFirstItem.asStateFlow()

    private var currentItems: List<PostContentModel> = emptyList()

    init {
        loadImageItem()
    }

    private fun loadImageItem() {
        viewModelScope.launch {
            try {
                currentItems = postRepository.getRandomPostList(10).getOrThrow()
                _mainItems.update { Result.success(currentItems.toList()) }
            } catch (e: Exception) {
                Log.e(MainPostItemViewModel::class.simpleName, e.message.orEmpty())
                _mainItems.update { Result.failure(e) }
            }
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

        _mainItems.update { Result.success(currentItems.toList()) }
        currentItems.find { new.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }

    fun fetchDetailFirst(item: PostContentModel) {
        currentItems.find { item.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }
}