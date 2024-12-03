package com.kolown.search

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.model.PostContentModel
import com.kolown.model.Tag
import com.kolown.search.component.PostHeader
import com.kolown.search.component.PostItem
import com.kolown.search.component.SearchResult
import com.kolown.search.component.TagItem
import com.kolown.search.component.TagSearchBar

@Composable
internal fun SearchRoute(
    padding: PaddingValues = PaddingValues(),
    navigateToSearchDetail: () -> Unit,
    viewModel: SearchViewModel
) {
    val searchResultTag = viewModel.searchResult.collectAsLazyPagingItems()
    val searchResultPost = viewModel.resultPostList.collectAsLazyPagingItems()
    val tag by viewModel.tag.collectAsStateWithLifecycle()
    val searchText by viewModel.searchQuery.collectAsStateWithLifecycle()

    SearchScreen(
        padding = padding,
        navigateToSearchDetail = navigateToSearchDetail,
        searchResultTag = searchResultTag,
        searchResultPost = searchResultPost,
        tag = tag,
        searchText = searchText,
        setSearchQuery = viewModel::setSearchQuery,
        setPage = viewModel::setPage,
        setTag = viewModel::setTag
    )
}

@Composable
private fun SearchScreen(
    padding: PaddingValues = PaddingValues(),
    navigateToSearchDetail: () -> Unit,
    searchResultTag: LazyPagingItems<Tag>,
    searchResultPost: LazyPagingItems<PostContentModel>,
    tag: Tag?,
    searchText: String,
    setSearchQuery: (String) -> Unit,
    setPage: (Int) -> Unit,
    setTag: (Tag) -> Unit
) {

    var focusState by remember { mutableStateOf(false) }


    val focusManager = LocalFocusManager.current
    LaunchedEffect(focusState) {
        if (!focusState) {
            focusManager.clearFocus()
            setSearchQuery(tag?.name ?: "")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TagSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(54.dp),
            text = if (focusState) searchText else tag?.name ?: "",
            onValueChange = setSearchQuery,
            onFocusChange = {
                focusState = it
            },
            focusState = focusState,
            onBackButtonClicked = {
                focusState = false
            },
            onClearClick = {
                setSearchQuery("")
            }
        )

        if (searchText.isEmpty() && !focusState) Text(
            text = "검색어를 입력하세요",
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(span = { GridItemSpan(3) }) {
                    tag?.let {
                        PostHeader(it)
                    } ?: run {
                        Log.e("test", "tag is null")
                    }

                }
                items(searchResultPost.itemCount) { index ->
                    searchResultPost[index]?.let { post ->
                        PostItem(
                            post,
                            onClick = {
                                setPage(index)
                                navigateToSearchDetail()
                            }
                        )
                    }
                }
            }

            SearchResult(focusState, searchResultTag,searchText) {
                setTag(it)
                focusState = false
            }
        }
    }
}


