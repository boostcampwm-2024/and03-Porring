package com.kolown.login

import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AuthRepository
import com.kolown.data.repository.UserRepository
import com.kolown.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private var _loginState: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    private var _isEmailLogin = MutableStateFlow(false)
    val isEmailLogin = _isEmailLogin.asStateFlow()

    fun handleSignIn(credential: Credential) {
        viewModelScope.launch {
            _loginState.update { UiState.Loading }

            authRepository.signInWithCredential(credential)
                .onSuccess {
                    _loginState.update { UiState.Success("로그인 완료") }
                    userRepository.createUserData()
                }
                .onFailure { e -> _loginState.update { UiState.Failure(e) } }
        }
    }

    fun changeEmailLogin(isEmail: Boolean) {
        _isEmailLogin.update { isEmail }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _loginState.update { UiState.Loading }

            authRepository.signInWithEmailAndPassword(email, password)
                .onSuccess {
                    _loginState.update { UiState.Success("로그인 완료") }
                    userRepository.createUserData()
                }
                .onFailure { e -> _loginState.update { UiState.Failure(e) } }
        }
    }
}