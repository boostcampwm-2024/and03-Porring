package com.kolown.main

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.common.component.NetworkStateManager
import com.kolown.data.repository.AuthRepository
import com.kolown.data.repository.PostRepository
import com.kolown.data.repository.RemoteConfigRepository
import com.kolown.model.InitUiState
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.SnackBarEvent
import com.kolown.model.UploadModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    private val _versionNameFlow = MutableSharedFlow<String>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val versionNameFlow = _versionNameFlow.asSharedFlow()


    private var _loginState = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()

    private val _uploadUiState = MutableStateFlow<InitUiState<Boolean>>(InitUiState.Init)
    val upLoadUiState = _uploadUiState.asStateFlow()

    private var _uploadModel = MutableStateFlow(UploadModel("", "", emptyList()))
    val uploadModel = _uploadModel.asStateFlow()

    private val _mainItems = MutableStateFlow<List<PostContentModel>>(emptyList())
    val mainItems = _mainItems.asStateFlow()

    private val _detailFirstItem =
        MutableStateFlow(PostContentModel("", "", "", "", "", emptyList(), false, emptyList()))
    val detailFirstItem = _detailFirstItem.asStateFlow()

    private val _snackBarFlow = MutableSharedFlow<SnackBarEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    private var currentItems: List<PostContentModel> = emptyList()

    private var randomType = listOf("A", "B", "C", "D", "E").random()

    init {
        updateLoginState()
        loadImageItem()
    }


    fun getVersionName() {
        viewModelScope.launch {
            remoteConfigRepository.getVersionName()?.let {
                _versionNameFlow.emit(it)
            }
        }
    }

    fun postSnackBarData(data: SnackBarEvent) {
        viewModelScope.launch {
            _snackBarFlow.emit(data)
        }
    }

    fun updateLoginState() {
        _loginState.value = authRepository.checkUserLoggedIn()
    }

    fun uploadPost(webPUri: String, description: String, categoryItems: List<String>) {
        viewModelScope.launch {
            _uploadModel.update {
                UploadModel(
                    imgUri = webPUri,
                    description = description,
                    categoryItems = categoryItems
                )
            }
            //네트워크 체크
            if (!NetworkStateManager.checkNetworkState(appContext)) {
                _uploadUiState.update { InitUiState.Failure(Exception()) }
                return@launch
            }



            _uploadUiState.value = InitUiState.Loading
            postRepository.uploadPost(
                fileUri = webPUri.toUri(),
                description = description,
                tags = categoryItems
            ).onSuccess {
                _uploadUiState.update { InitUiState.Success(true) }
                _uploadModel.update { UploadModel("", "", emptyList()) }
            }.onFailure { error ->
                _uploadUiState.update { InitUiState.Failure(error) }
            }
        }
    }

    fun resetUploadState() {
        _uploadUiState.update { InitUiState.Init }
    }

    fun selectReaction(new: PostContentModel, reaction: Reactions) {
        currentItems = currentItems.map { prev ->
            prev.takeIf { prev.postId == new.postId }?.let {
                if (prev.myReaction == null) {
                    prev.copy(
                        reactions = prev.reactions + reaction, myReaction = reaction
                    )
                } else {
                    if (prev.myReaction == reaction) {
                        prev.copy(
                            reactions = prev.reactions - reaction, myReaction = null
                        )
                    } else {
                        prev.copy(
                            reactions = prev.reactions - prev.myReaction!! + reaction,
                            myReaction = reaction
                        )
                    }
                }
            } ?: prev
        }

        _mainItems.update { currentItems }
        currentItems.find { new.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }

    fun updateFollow(id: String) {
        currentItems = currentItems.map {
            if (it.authorId == id) {
                it.copy(isFollower = !it.isFollower)
            } else {
                it
            }
        }

        _mainItems.update { currentItems }
        currentItems.find { it.authorId == id }?.let { new ->
            _detailFirstItem.update { new }
        }
    }

    fun fetchDetailFirst(item: PostContentModel) {
        currentItems.find { item.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }

    fun refreshImageItem() {
        randomType = listOf("A", "B", "C", "D", "E").random()
        loadImageItem()
    }

    private fun loadImageItem() {
        postRepository.getRandomPostList(10, randomType)
            .onEach { items ->
                currentItems = items.shuffled()
                _mainItems.update { currentItems }
            }
            .catch {
                Log.e("MainViewModel", "loadImageItem: $it")
            }
            .launchIn(viewModelScope)

    }
}
