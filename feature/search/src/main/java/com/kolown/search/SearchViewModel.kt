package com.kolown.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.TagRepository
import com.kolown.model.PostContentModel
import com.kolown.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _tag = MutableStateFlow<Tag?>(null)
    val tag = _tag.asStateFlow()

    private var _firstPage = 0
    val firstPage get() = _firstPage

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult = _searchQuery.debounce(SEARCH_DEBOUNCE_TIME_MILLIS)
        .filter { it.isNotBlank() }
        .distinctUntilChanged() //같은거 반응 안함.(근데 stateflow라 어차피 반응 안할듯?)
        .flatMapLatest {
            tagRepository.getTagBySearch(it)
        }.cachedIn(viewModelScope)

    fun setSearchQuery(searchText: String) {
        _searchQuery.value = searchText
    }


    fun setTag(tag: Tag) {
        _tag.value = tag
        _searchQuery.value = "# ${tag.name}"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val resultPostList = _tag.filter {
        it != null
    }.flatMapLatest { tag ->
        tag?.let {
            postRepository.getPostBySearch(tag.id)
                .onStart { emit(PagingData.empty()) }
        } ?: flow { emit(PagingData.empty()) }
    }

    fun setPage(page : Int) {
        _firstPage = page
    }


    companion object {
        const val SEARCH_DEBOUNCE_TIME_MILLIS = 300L
    }
}
