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
                    1,
                    "",
                    "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
                    "",
                    Reactions.LOVE,
                    false,
                    listOf(
                        Reactions.SURPRISE,
                        Reactions.SMILE,
                        Reactions.STAR,
                        Reactions.THUMB,
                        Reactions.LOVE,
                        Reactions.HEART
                    )
                ),
                ImageItem(
                    2,
                    "",
                    "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
                    "",
                    Reactions.STAR,
                    true,
                    listOf(
                        Reactions.SURPRISE,
                        Reactions.SMILE,
                        Reactions.LOVE,
                        Reactions.HEART
                    )
                ),
                ImageItem(
                    3,
                    "",
                    "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
                    "",
                    null,
                    false,
                    emptyList()
                ),
            )
        }
    }

    fun followUser(id: Long) {
        _mainFeedImageItems.update {
            it.map { imageItem ->
                if (imageItem.id == id) {
                    imageItem.copy(isFollowed = !imageItem.isFollowed)
                } else {
                    imageItem
                }
            }
        }
    }

}