package com.kolown.porring

import androidx.lifecycle.ViewModel
import com.kolown.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserStateViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var _loginState = MutableStateFlow<Boolean>(false)
    val loginState = _loginState.asStateFlow()

    init {
        updateLoginState()
    }

    fun updateLoginState() {
        _loginState.value = authRepository.checkUserLoggedIn()
    }
}