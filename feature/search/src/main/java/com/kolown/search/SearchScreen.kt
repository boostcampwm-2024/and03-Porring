package com.kolown.search

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.kolown.search.component.TagSearchBar

@Composable
internal fun SearchRoute(
    padding: PaddingValues = PaddingValues(),
) {
    SearchScreen(
        padding = padding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    padding: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel = hiltViewModel(),
) {

    val searchResultTag = viewModel.searchResult.collectAsLazyPagingItems()
    val searchText by viewModel.searchQuery.collectAsStateWithLifecycle()

    var isSearchingActivated by remember { mutableStateOf(false) }
//    LaunchedEffect(searchResultTag){
//        searchResultTag.refresh()
//    }

//    SearchBar(
//        query = searchText,
//        onQueryChange = viewModel::setSearchQuery,
//        onSearch = {},
//        active = isSearchingActivated,
//        onActiveChange = {
//            isSearchingActivated = true
//        }, //the callback to be invoked when this search bar's active state is changed
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(searchResultTag.itemCount) { index ->
//                searchResultTag[index]?.let {
//                    Text(
//                        text = it.name,
//                        modifier = Modifier.padding(
//                            start = 8.dp,
//                            top = 4.dp,
//                            end = 8.dp,
//                            bottom = 4.dp
//                        )
//                    )
//                }
//
//            }
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TagSearchBar(modifier = Modifier
            .fillMaxWidth().height(54.dp), text = "무니") {

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "SearchScreen", style = MaterialTheme.typography.displayLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchScreen() {
    SearchScreen()
}
