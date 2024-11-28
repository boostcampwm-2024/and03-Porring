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
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.search.component.PostHeader
import com.kolown.search.component.PostItem
import com.kolown.search.component.TagItem
import com.kolown.search.component.TagSearchBar

@Composable
internal fun SearchRoute(
    padding: PaddingValues = PaddingValues(),
    navigateToSearchDetail : () -> Unit,
    viewModel: SearchViewModel
) {
    SearchScreen(
        padding = padding,
        viewModel = viewModel,
        navigateToSearchDetail = navigateToSearchDetail
    )
}

@Composable
private fun SearchScreen(
    padding: PaddingValues = PaddingValues(),
    navigateToSearchDetail : () -> Unit,
    viewModel: SearchViewModel,
) {

    val searchResultTag = viewModel.searchResult.collectAsLazyPagingItems()
    val searchResultPost = viewModel.resultPostList.collectAsLazyPagingItems()
    val tag by viewModel.tag.collectAsStateWithLifecycle()
    val searchText by viewModel.searchQuery.collectAsStateWithLifecycle()
    var focusState by remember { mutableStateOf(false) }


    val focusManager = LocalFocusManager.current
    LaunchedEffect(focusState) {
        if (!focusState) {
            focusManager.clearFocus()
            viewModel.setSearchQuery(tag?.name ?: "")
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
            onValueChange = viewModel::setSearchQuery,
            onFocusChange = {
                focusState = it
            },
            focusState = focusState,
            onBackButtonClicked = {
                focusState = false
            }
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
                                viewModel.setPage(index)
                                navigateToSearchDetail()
                            }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .animateContentSize()
                    .height(if (focusState) 800.dp else 0.dp)
                    .fillMaxWidth()

            ) {
                items(searchResultTag.itemCount) { index ->
                    searchResultTag[index]?.let { tag ->
                        TagItem(tag) {
                            viewModel.setTag(it)
                            focusState = false
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ExpandableColumnExample() {
    // 상태: 확장 여부
    var isExpanded by remember { mutableStateOf(false) }

    // 확장 높이 애니메이션
    val animatedHeight by animateDpAsState(targetValue = if (isExpanded) 200.dp else 0.dp)

    Column(modifier = Modifier.fillMaxSize()) {
        // 첫 번째 Custom Composable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f) // zIndex를 높여 두 번째 Box 위로 렌더링
                .background(Color.Gray)
        ) {
            Column {
                Text(
                    text = "Click the button below to expand",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
                Button(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = if (isExpanded) "Collapse" else "Expand")
                }

                // 애니메이션 확장 Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeight)
                        .background(Color.Blue)
                ) {
                    Text(
                        text = "Expanded Content",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        // 두 번째 Composable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Green)
        ) {
            Text(
                text = "Second Composable",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}

