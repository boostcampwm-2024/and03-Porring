package com.kolown.home

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

    private val pageSize = 5

    init {
        loadImageItem()
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

    fun selectReaction(id: Long, reaction: Reactions) {
        _mainFeedImageItems.update {
            it.map { imageItem ->
                if (imageItem.id == id) {
                    imageItem.copy(reactions = reaction)
                } else {
                    imageItem
                }
            }
        }
    }

    // todo Firebase 연결되면 서버로부터 값을 가져오는 작업 해야 됨.
    // DocumentSnapshot으로 마지막 아이템 저장 => NextPage 가져올 때는 startAfter()로 다음 거부터 가져오도록!
    fun loadImageItem() {
        addDummyList()
    }

    private fun addDummyList() {
        val dummyList = listOf(
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
            ImageItem(
                4,
                "",
                "https://echo.unicomm.fsu.edu/3.3/img/placeholders/ratio-4-5.png",
                "",
                Reactions.SMILE,
                false,
                listOf(
                    Reactions.THUMB,
                    Reactions.LOVE,
                    Reactions.HEART,
                    Reactions.SURPRISE,
                    Reactions.SMILE,
                    Reactions.STAR,
                    Reactions.SURPRISE,
                    Reactions.SURPRISE,
                    Reactions.SMILE,
                    Reactions.STAR,
                )
            ),
        )

        _mainFeedImageItems.update {
            it + dummyList
        }
    }

}