package com.kolown.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolown.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var _logoutEnd = MutableSharedFlow<Boolean>()
    val logoutEnd = _logoutEnd.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            val result = authRepository.logout()

            when {
                result.isSuccess -> _logoutEnd.emit(true)
                result.isFailure -> _logoutEnd.emit(false)
            }
        }
    }
}