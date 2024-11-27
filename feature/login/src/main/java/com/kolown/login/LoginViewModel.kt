package com.kolown.login

import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AuthRepository
import com.kolown.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private var _loginEnd = MutableSharedFlow<Boolean>()
    val loginEnd = _loginEnd.asSharedFlow()

    private var _isEmailLogin = MutableStateFlow(false)
    val isEmailLogin = _isEmailLogin.asStateFlow()

    fun handleSignIn(credential: Credential) {
        viewModelScope.launch {
            val result = authRepository.signInWithCredential(credential)

            when {
                result.isSuccess -> {
                    _loginEnd.emit(true)
                    userRepository.createUserData()
                }

                result.isFailure -> {
                    _loginEnd.emit(false)
                }
            }
        }
    }

    fun changeEmailLogin(isEmail: Boolean) {
        _isEmailLogin.update { isEmail }
    }
}
