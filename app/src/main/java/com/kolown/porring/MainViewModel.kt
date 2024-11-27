package com.kolown.porring

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AuthRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.InitUiState
import com.kolown.model.PostContentModel
import com.kolown.model.Reactions
import com.kolown.model.UploadModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    private var _loginState = MutableStateFlow(false)
    val loginState = _loginState.asStateFlow()

    private val _uploadUiState = MutableStateFlow<InitUiState<Boolean>>(InitUiState.Init)
    val upLoadUiState = _uploadUiState.asStateFlow()

    private var _uploadModel = MutableStateFlow(UploadModel("", "", emptyList()))
    val uploadModel = _uploadModel.asStateFlow()

    private val _mainItems = MutableStateFlow<Flow<List<PostContentModel>>>(flow { })
    val mainItems = _mainItems.asStateFlow()

    private val _detailFirstItem =
        MutableStateFlow(PostContentModel("", "", "", "", "", emptyList(), false, emptyList()))
    val detailFirstItem = _detailFirstItem.asStateFlow()

    private var currentItems: List<PostContentModel> = emptyList()

    init {
        updateLoginState()
        loadImageItem()
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

        _mainItems.update { flow { emit(currentItems) } }
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

        _mainItems.update { flow { emit(currentItems) } }
    }

    fun fetchDetailFirst(item: PostContentModel) {
        currentItems.find { item.postId == it.postId }?.let { new ->
            _detailFirstItem.update { new }
        }
    }

    private fun loadImageItem() {
        postRepository.getRandomPostList(10).let { flow ->
            _mainItems.update { flow }
            flow.onEach { currentItems = it }.launchIn(viewModelScope)
        }
    }
}