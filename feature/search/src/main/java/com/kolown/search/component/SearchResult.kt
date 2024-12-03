package com.kolown.search.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.kolown.designsystem.ui.theme.Gray
import com.kolown.model.Tag

@Composable
fun SearchResult(
    focusState: Boolean,
    searchResultTag: LazyPagingItems<Tag>,
    text: String,
    onTagClicked: (tag: Tag) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .animateContentSize()
            .width(if (focusState) 10000.dp else 0.dp)
            .height(if (focusState) 10000.dp else 0.dp) //매우 크게 놓으면 사이즈에 맞게 출력됨.
            .border(1.dp, Color.Black)
    ) {
        if (text.isBlank()) {
            Text(
                "검색어를 입력해주세요",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center),
                color = Gray
            )
            return@Box
        }
        if (searchResultTag.itemCount == 0) {
            Text(
                "검색 결과가 없습니다.",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center),
                color = Gray
            )
            return@Box
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(searchResultTag.itemCount) { index ->
                searchResultTag[index]?.let { tag ->
                    TagItem(tag, onTagClicked)
                }
            }
        }
    }

}
