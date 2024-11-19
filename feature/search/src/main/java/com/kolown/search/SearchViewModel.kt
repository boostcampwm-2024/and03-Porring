package com.kolown.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class SearchViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchText = _searchText.debounce(SEARCH_DEBOUNCE_TIME_MILLIS)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest {

        }



    fun setSearchText(searchText: String) {
        _searchText.value = searchText
    }

    private fun searchTag() = flow{

    }

    companion object {
        const val SEARCH_DEBOUNCE_TIME_MILLIS = 500L
    }
}
