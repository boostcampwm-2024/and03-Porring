package com.kolown.porring

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AuthRepository
import com.kolown.data.repository.PostRepository
import com.kolown.model.InitUiState
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        updateLoginState()
    }

    fun updateLoginState() {
        _loginState.value = authRepository.checkUserLoggedIn()
    }

    fun uploadPost(webPUri: String, description: String, categoryItems: List<String>) {
        viewModelScope.launch {
            _uploadUiState.value = InitUiState.Loading
            postRepository.uploadPost(
                fileUri = webPUri.toUri(),
                description = description,
                tags = categoryItems
            ).onSuccess {
                _uploadUiState.update { InitUiState.Success(true) }
            }.onFailure { error ->
                _uploadUiState.update { InitUiState.Failure(error) }
            }
        }
    }

    fun resetUploadState() {
        _uploadUiState.update { InitUiState.Init }
    }
}