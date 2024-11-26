package com.kolown.porring

import android.widget.Toast
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kolown.model.InitUiState
import com.kolown.model.UiState
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.porring.component.MainBottomBar
import com.kolown.porring.component.MainNavHost
import com.kolown.porring.navigation.MainMenu
import com.kolown.porring.navigation.MainNavigator
import com.kolown.porring.navigation.rememberMainNavigator
import com.kolown.porring.viewmodel.MainPostItemViewModel
import com.kolown.porring.viewmodel.UserStateViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    mainViewModel: MainViewModel = hiltViewModel(),
    mainPostItemViewModel: MainPostItemViewModel = hiltViewModel(),
) {
    val mainItems by mainPostItemViewModel.mainItems.collectAsStateWithLifecycle()
    val detailFirstItem by mainPostItemViewModel.detailFirstItem.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoggedIn by mainViewModel.loginState.collectAsStateWithLifecycle()

    val uploadState by mainViewModel.upLoadUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uploadState) {
        when(uploadState) {
            InitUiState.Init -> { }
            is InitUiState.Failure -> Toast.makeText(context, "업로드 실패", Toast.LENGTH_SHORT).show()
            InitUiState.Loading -> Toast.makeText(context, "업로드 중입니다.", Toast.LENGTH_SHORT).show()
            is InitUiState.Success -> Toast.makeText(context, "업로드 성공", Toast.LENGTH_SHORT).show()
        }
    }

    MainScreenContent(
        mainItems = mainItems,
        onSelectReaction = mainPostItemViewModel::selectReaction,
        detailFirstItem = detailFirstItem,
        fetchDetailFirst = mainPostItemViewModel::fetchDetailFirst,
        updateFollow = mainPostItemViewModel::updateFollow,
        isLoggedIn = isLoggedIn,
        updateLoginState = mainViewModel::updateLoginState,
        navigator = navigator,
        snackBarHostState = snackBarHostState,
        uploadPost = mainViewModel::uploadPost
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MainScreenContent(
    mainItems: Flow<List<PostContentModel>>,
    onSelectReaction: (PostContentModel, Reactions) -> Unit,
    detailFirstItem: PostContentModel,
    fetchDetailFirst: (PostContentModel) -> Unit,
    updateFollow: (String) -> Unit,
    isLoggedIn: Boolean,
    updateLoginState: () -> Unit,
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    snackBarHostState: SnackbarHostState,
    uploadPost: (String, String, List<String>) -> Unit
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                mainItems = mainItems,
                onSelectReaction = onSelectReaction,
                detailFirstItem = detailFirstItem,
                fetchDetailFirst = fetchDetailFirst,
                updateFollow = updateFollow,
                isLoggedIn = isLoggedIn,
                updateLoginState = updateLoginState,
                navigator = navigator,
                padding = padding,
                uploadPost = uploadPost
            )
        },
        bottomBar = {
            MainBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(top = 12.dp, bottom = 16.dp),
                visible = navigator.isShowBottomBar(),
                menus = MainMenu.entries.toPersistentList(),
                currentMenu = navigator.currentMenu,
                onMenuSelected = { navigator.navigate(it) }
            )
        }
    )
}