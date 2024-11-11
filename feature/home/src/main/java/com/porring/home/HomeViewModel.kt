package com.porring.home

import androidx.lifecycle.ViewModel
import com.kolown.model.ImageItem
import com.kolown.model.Reactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {
    private val _mainFeedImageItems = MutableStateFlow<List<ImageItem>>(emptyList())
    val mainFeedImageItems = _mainFeedImageItems.asStateFlow()

    init {
        _mainFeedImageItems.update {
            listOf(
                ImageItem(
                    1, "", "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
                    "", Reactions.LOVE, false, emptyList()
                )
            )
        }
    }

}